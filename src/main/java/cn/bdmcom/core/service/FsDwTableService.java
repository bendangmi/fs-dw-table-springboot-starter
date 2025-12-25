package cn.bdmcom.core.service;

import cn.bdmcom.core.api.FsDwTableApi;
import cn.bdmcom.core.domain.FsDwConstants;
import cn.bdmcom.core.domain.req.BatchCreateTableReq;
import cn.bdmcom.core.domain.req.BatchDeleteTableReq;
import cn.bdmcom.core.domain.req.CreateTableReq;
import cn.bdmcom.core.domain.req.UpdateTableReq;
import cn.bdmcom.core.domain.res.*;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;
import cn.bdmcom.support.BitableException;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 飞书多维表格数据表服务。
 *
 * <p>提供数据表级别 CRUD 与列表查询封装。</p>
 */
@Slf4j
public class FsDwTableService {

    private static final Integer SUCCESS_CODE = 0;

    @Autowired
    private FsDwTokenService fsDwTokenService;

    @Autowired
    private FsDwTableApi fsDwTableApi;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 新增数据表
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  应用令牌
     * @param req       新增数据表请求
     * @return 响应结果
     */
    public CreateTableRes createTable(String appId, String appSecret, String appToken, CreateTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]新增数据表请求不能为空");
        validateAppToken(appToken);
        String res = fsDwTableApi.createTable(buildAuthorization(appId, appSecret), appToken, req);
        CreateTableRes result = parseResponse("新增数据表", res, CreateTableRes.class);
        log.info("[飞书多维表格]新增数据表成功, code={}", result.getCode());
        return result;
    }

    /**
     * 批量新增数据表
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  应用令牌
     * @param req       批量新增数据表请求
     * @return 响应结果
     */
    public BatchCreateTableRes batchCreateTable(String appId, String appSecret, String appToken, BatchCreateTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量新增数据表请求不能为空");
        validateAppToken(appToken);
        String res = fsDwTableApi.batchCreateTable(buildAuthorization(appId, appSecret), appToken, req);
        BatchCreateTableRes result = parseResponse("批量新增数据表", res, BatchCreateTableRes.class);
        log.info("[飞书多维表格]批量新增数据表成功, code={}", result.getCode());
        return result;
    }

    /**
     * 更新数据表
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  应用令牌
     * @param tableId   数据表ID
     * @param req       更新数据表请求
     * @return 响应结果
     */
    public UpdateTableRes updateTable(String appId, String appSecret, String appToken, String tableId, UpdateTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]更新数据表请求不能为空");
        validateAppToken(appToken);
        validateTableId(tableId);
        String res = fsDwTableApi.updateTable(buildAuthorization(appId, appSecret), appToken, tableId, req);
        UpdateTableRes result = parseResponse("更新数据表", res, UpdateTableRes.class);
        log.info("[飞书多维表格]更新数据表成功, code={}", result.getCode());
        return result;
    }

    /**
     * 删除数据表
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  应用令牌
     * @param tableId   数据表ID
     * @return 响应结果
     */
    public DeleteTableRes deleteTable(String appId, String appSecret, String appToken, String tableId) {
        validateAppToken(appToken);
        validateTableId(tableId);
        String res = fsDwTableApi.deleteTable(buildAuthorization(appId, appSecret), appToken, tableId);
        DeleteTableRes result = parseResponse("删除数据表", res, DeleteTableRes.class);
        log.info("[飞书多维表格]删除数据表成功, code={}", result.getCode());
        return result;
    }

    /**
     * 批量删除数据表
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  应用令牌
     * @param req       批量删除数据表请求
     * @return 响应结果
     */
    public BatchDeleteTableRes batchDeleteTable(String appId, String appSecret, String appToken, BatchDeleteTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量删除数据表请求不能为空");
        validateAppToken(appToken);
        String res = fsDwTableApi.batchDeleteTable(buildAuthorization(appId, appSecret), appToken, req);
        BatchDeleteTableRes result = parseResponse("批量删除数据表", res, BatchDeleteTableRes.class);
        log.info("[飞书多维表格]批量删除数据表成功, code={}", result.getCode());
        return result;
    }

    /**
     * 列出数据表
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  应用令牌
     * @param pageSize  分页大小
     * @param pageToken 分页标记
     * @return 响应结果
     */
    public ListTableRes listTables(String appId, String appSecret, String appToken, Integer pageSize, String pageToken) {
        validateAppToken(appToken);
        String res = fsDwTableApi.listTables(buildAuthorization(appId, appSecret), appToken, pageSize, pageToken);
        ListTableRes result = parseResponse("列出数据表", res, ListTableRes.class);
        log.info("[飞书多维表格]列出数据表成功, code={}", result.getCode());
        return result;
    }

    /**
     * 构建授权信息
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @return 授权信息
     */
    private String buildAuthorization(String appId, String appSecret) {
        String token = fsDwTokenService.getToken(appId, appSecret);
        BitableAssert.notBlank(token, BitableErrorCode.TOKEN_ACQUIRE_FAILED, "[飞书多维表格]token获取失败");
        return FsDwConstants.AUTHORIZATION_PREFIX + token;
    }

    /**
     * 解析响应结果
     *
     * @param action 操作名称
     * @param res    响应结果
     * @param clazz  响应结果类型
     * @param <T>    响应结果类型
     * @return 响应结果
     */
    private <T> T parseResponse(String action, String res, Class<T> clazz) {
        BitableAssert.notBlank(res, BitableErrorCode.FEISHU_RESPONSE_EMPTY, "[飞书多维表格][{}]响应为空", action);
        try {
            T result = objectMapper.readValue(res, clazz);
            BitableAssert.notNull(result, BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, "[飞书多维表格][{}]解析结果为空", action);
            if (result instanceof AbstractRes<?> abstractRes) {
                BitableAssert.isTrue(SUCCESS_CODE.equals(abstractRes.getCode()), BitableErrorCode.FEISHU_API_ERROR,
                        "[飞书多维表格][{}]失败, code={}, msg={}", action, abstractRes.getCode(), abstractRes.getMsg());
            }
            return result;
        } catch (BitableException e) {
            throw e;
        } catch (Exception e) {
            String message = StrUtil.format("[飞书多维表格][{}]解析响应失败: {}", action, res);
            throw new BitableException(BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, message, e);
        }
    }

    /**
     * 校验appToken
     *
     * @param appToken 应用凭证
     */
    private void validateAppToken(String appToken) {
        BitableAssert.notBlank(appToken, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]appToken不能为空");
    }

    /**
     * 校验tableId
     *
     * @param tableId 数据表ID
     */
    private void validateTableId(String tableId) {
        BitableAssert.notBlank(tableId, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]tableId不能为空");
    }
}
