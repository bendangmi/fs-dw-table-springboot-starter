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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * 飞书多维表格数据表服务。
 *
 * <p>提供数据表级别 CRUD 与列表查询封装。</p>
 */
@Service
@Slf4j
public class FsDwTableService {

    private static final Integer SUCCESS_CODE = 0;

    @Resource
    private FsDwTokenService fsDwTokenService;

    @Resource
    private FsDwTableApi fsDwTableApi;

    @Resource
    private ObjectMapper objectMapper;

    public CreateTableRes createTable(String appId, String appSecret, String appToken, CreateTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]新增数据表请求不能为空");
        validateAppToken(appToken);
        String res = fsDwTableApi.createTable(buildAuthorization(appId, appSecret), appToken, req);
        CreateTableRes result = parseResponse("新增数据表", res, CreateTableRes.class);
        log.info("[飞书多维表格]新增数据表成功, code={}", result.getCode());
        return result;
    }

    public BatchCreateTableRes batchCreateTable(String appId, String appSecret, String appToken, BatchCreateTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量新增数据表请求不能为空");
        validateAppToken(appToken);
        String res = fsDwTableApi.batchCreateTable(buildAuthorization(appId, appSecret), appToken, req);
        BatchCreateTableRes result = parseResponse("批量新增数据表", res, BatchCreateTableRes.class);
        log.info("[飞书多维表格]批量新增数据表成功, code={}", result.getCode());
        return result;
    }

    public UpdateTableRes updateTable(String appId, String appSecret, String appToken, String tableId, UpdateTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]更新数据表请求不能为空");
        validateAppToken(appToken);
        validateTableId(tableId);
        String res = fsDwTableApi.updateTable(buildAuthorization(appId, appSecret), appToken, tableId, req);
        UpdateTableRes result = parseResponse("更新数据表", res, UpdateTableRes.class);
        log.info("[飞书多维表格]更新数据表成功, code={}", result.getCode());
        return result;
    }

    public DeleteTableRes deleteTable(String appId, String appSecret, String appToken, String tableId) {
        validateAppToken(appToken);
        validateTableId(tableId);
        String res = fsDwTableApi.deleteTable(buildAuthorization(appId, appSecret), appToken, tableId);
        DeleteTableRes result = parseResponse("删除数据表", res, DeleteTableRes.class);
        log.info("[飞书多维表格]删除数据表成功, code={}", result.getCode());
        return result;
    }

    public BatchDeleteTableRes batchDeleteTable(String appId, String appSecret, String appToken, BatchDeleteTableReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量删除数据表请求不能为空");
        validateAppToken(appToken);
        String res = fsDwTableApi.batchDeleteTable(buildAuthorization(appId, appSecret), appToken, req);
        BatchDeleteTableRes result = parseResponse("批量删除数据表", res, BatchDeleteTableRes.class);
        log.info("[飞书多维表格]批量删除数据表成功, code={}", result.getCode());
        return result;
    }

    public ListTableRes listTables(String appId, String appSecret, String appToken, Integer pageSize, String pageToken) {
        validateAppToken(appToken);
        String res = fsDwTableApi.listTables(buildAuthorization(appId, appSecret), appToken, pageSize, pageToken);
        ListTableRes result = parseResponse("列出数据表", res, ListTableRes.class);
        log.info("[飞书多维表格]列出数据表成功, code={}", result.getCode());
        return result;
    }

    private String buildAuthorization(String appId, String appSecret) {
        String token = fsDwTokenService.getToken(appId, appSecret);
        BitableAssert.notBlank(token, BitableErrorCode.TOKEN_ACQUIRE_FAILED, "[飞书多维表格]token获取失败");
        return FsDwConstants.AUTHORIZATION_PREFIX + token;
    }

    private <T> T parseResponse(String action, String res, Class<T> clazz) {
        BitableAssert.notBlank(res, BitableErrorCode.FEISHU_RESPONSE_EMPTY, "[飞书多维表格][{}]响应为空", action);
        try {
            T result = objectMapper.readValue(res, clazz);
            BitableAssert.notNull(result, BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, "[飞书多维表格][{}]解析结果为空", action);
            if (result instanceof AbstractRes) {
                AbstractRes<?> abstractRes =
                        (AbstractRes<?>) result;
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

    private void validateAppToken(String appToken) {
        BitableAssert.notBlank(appToken, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]appToken不能为空");
    }

    private void validateTableId(String tableId) {
        BitableAssert.notBlank(tableId, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]tableId不能为空");
    }
}
