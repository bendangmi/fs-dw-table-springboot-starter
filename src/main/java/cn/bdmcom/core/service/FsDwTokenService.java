package cn.bdmcom.core.service;

import cn.bdmcom.core.api.FsDwRecordApi;
import cn.bdmcom.core.domain.req.QueryTokenReq;
import cn.bdmcom.core.domain.res.QueryTokenRes;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;
import cn.bdmcom.support.BitableException;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 飞书多维表格 Token 服务。
 *
 * <p>负责拉取 app_access_token 并使用 Caffeine 进行高性能内存缓存，
 * 支持自动过期、并发安全和统计信息。</p>
 */
@Slf4j
public class FsDwTokenService {

    /**
     * 飞书接口统一成功码。
     */
    private static final Integer SUCCESS_CODE = 0;

    /**
     * 提前刷新缓冲（秒）。
     */
    private static final long TOKEN_REFRESH_BUFFER_SECONDS = 60L;

    /**
     * Token 缓存（按 appId + appSecret 维度）。
     * 使用 Caffeine 提供：
     * - 自动过期（2小时）
     * - 并发安全（基于 ConcurrentHashMap）
     * - 淘汰策略（LRU）
     * - 统计信息
     */
    private final Cache<String, String> tokenCache = Caffeine.newBuilder()
            // 飞书 token 默认 2 小时过期
            .expireAfterWrite(2, TimeUnit.HOURS)
            // 最多缓存 100 个 token
            .maximumSize(100)
            // 开启统计
            .recordStats()
            .build();

    @Autowired
    private FsDwRecordApi fsDwRecordApi;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取飞书多维表格 token。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @return token
     */
    public String getToken(String appId, String appSecret) {
        BitableAssert.notBlank(appId, BitableErrorCode.PARAM_REQUIRED, "[飞书Token]appId不能为空");
        BitableAssert.notBlank(appSecret, BitableErrorCode.PARAM_REQUIRED, "[飞书Token]appSecret不能为空");
        String cacheKey = buildCacheKey(appId, appSecret);

        // 尝试从缓存获取
        String token = tokenCache.getIfPresent(cacheKey);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }

        // 缓存未命中，获取新 token
        return refreshToken(cacheKey, appId, appSecret);
    }

    /**
     * 刷新 token 并更新缓存。
     *
     * @param cacheKey  缓存键
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @return 新的 token
     */
    private synchronized String refreshToken(String cacheKey, String appId, String appSecret) {
        // 双重检查，避免重复请求
        String existingToken = tokenCache.getIfPresent(cacheKey);
        if (StrUtil.isNotBlank(existingToken)) {
            return existingToken;
        }

        String res = fsDwRecordApi.getToken(QueryTokenReq.builder()
                .appId(appId)
                .appSecret(appSecret)
                .build());
        log.info("[飞书Token]获取响应成功");
        QueryTokenRes queryTokenRes = parseTokenResponse(res);
        BitableAssert.isTrue(SUCCESS_CODE.equals(queryTokenRes.getCode()), BitableErrorCode.TOKEN_ACQUIRE_FAILED,
                "[飞书Token]获取失败, code={}, msg={}", queryTokenRes.getCode(), queryTokenRes.getMsg());
        BitableAssert.notBlank(queryTokenRes.getAppAccessToken(), BitableErrorCode.TOKEN_ACQUIRE_FAILED,
                "[飞书Token]获取失败, app_access_token为空");

        // 计算实际过期时间（减去缓冲时间）
        long ttlMillis = buildTtlMillis(queryTokenRes.getExpire());

        // 存入缓存（使用 Caffeine 的 expireAfterWrite）
        tokenCache.put(cacheKey, queryTokenRes.getAppAccessToken());

        log.info("[飞书Token]token已缓存, expire={}秒, ttl={}毫秒", queryTokenRes.getExpire(), ttlMillis);
        return queryTokenRes.getAppAccessToken();
    }

    /**
     * 获取缓存统计信息（用于监控）。
     *
     * @return 缓存统计信息
     */
    public CacheStats getCacheStats() {
        return tokenCache.stats();
    }

    /**
     * 手动清除指定 token 缓存。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     */
    public void evictToken(String appId, String appSecret) {
        String cacheKey = buildCacheKey(appId, appSecret);
        tokenCache.invalidate(cacheKey);
        log.info("[飞书Token]已清除缓存, cacheKey={}", cacheKey);
    }

    /**
     * 清除所有 token 缓存。
     */
    public void evictAll() {
        tokenCache.invalidateAll();
        log.info("[飞书Token]已清除所有缓存");
    }

    /**
     * 解析飞书多维表格 token 响应。
     *
     * @param res 响应
     * @return 解析结果
     */
    private QueryTokenRes parseTokenResponse(String res) {
        BitableAssert.notBlank(res, BitableErrorCode.FEISHU_RESPONSE_EMPTY, "[飞书Token]响应为空");
        try {
            QueryTokenRes result = objectMapper.readValue(res, QueryTokenRes.class);
            BitableAssert.notNull(result, BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, "[飞书Token]解析结果为空");
            return result;
        } catch (BitableException e) {
            throw e;
        } catch (Exception e) {
            throw new BitableException(BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR,
                    "[飞书Token]解析响应失败: " + res, e);
        }
    }

    /**
     * 构建缓存 key。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @return 缓存 key
     */
    private String buildCacheKey(String appId, String appSecret) {
        return appId + "_" + appSecret;
    }

    /**
     * 构建缓存 TTL。
     *
     * @param expireSeconds 过期时间（秒）
     * @return TTL（毫秒）
     */
    private long buildTtlMillis(Integer expireSeconds) {
        if (expireSeconds == null || expireSeconds <= 0) {
            return 60_000L;
        }
        long safeSeconds = Math.max(1L, expireSeconds - TOKEN_REFRESH_BUFFER_SECONDS);
        return safeSeconds * 1000L;
    }
}
