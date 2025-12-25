package cn.bdmcom.core.service;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import cn.bdmcom.core.api.FsDwRecordApi;
import cn.bdmcom.core.domain.ExpiringHashMap;
import cn.bdmcom.core.domain.req.QueryTokenReq;
import cn.bdmcom.core.domain.res.QueryTokenRes;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;
import cn.bdmcom.support.BitableException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 飞书多维表格 Token 服务。
 *
 * <p>负责拉取 app_access_token 并在内存中做短期缓存，避免频繁请求。</p>
 */
@Slf4j
public class FsDwTokenService {

    /**
     * 飞书接口统一成功码。
     */
    private static final Integer SUCCESS_CODE = 0;

    @Autowired
    private FsDwRecordApi fsDwRecordApi;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 内存缓存 token（按 appId + appSecret 维度）。
     */
    private static final ExpiringHashMap<String, String> TOKEN_MAP = new ExpiringHashMap<>();

    /**
     * 提前刷新缓冲（秒）。
     */
    private static final long TOKEN_REFRESH_BUFFER_SECONDS = 60L;

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

        String token = TOKEN_MAP.get(cacheKey);
        if (StrUtil.isNotBlank(token)) {
            return token;
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

        long ttlMillis = buildTtlMillis(queryTokenRes.getExpire());
        TOKEN_MAP.put(cacheKey, queryTokenRes.getAppAccessToken(), ttlMillis);
        return queryTokenRes.getAppAccessToken();
    }

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

    private String buildCacheKey(String appId, String appSecret) {
        return appId + "_" + appSecret;
    }

    private long buildTtlMillis(Integer expireSeconds) {
        if (expireSeconds == null || expireSeconds <= 0) {
            return 60_000L;
        }
        long safeSeconds = Math.max(1L, expireSeconds - TOKEN_REFRESH_BUFFER_SECONDS);
        return safeSeconds * 1000L;
    }
}
