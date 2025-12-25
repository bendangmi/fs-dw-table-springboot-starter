package cn.bdmcom.core.service;

import cn.bdmcom.core.api.FsDwRecordApi;
import cn.bdmcom.core.domain.FsDwConstants;
import cn.bdmcom.core.domain.req.*;
import cn.bdmcom.core.domain.res.*;
import cn.bdmcom.core.helper.FsDwRecordHelper;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;
import cn.bdmcom.support.BitableException;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 飞书多维表格记录服务。
 *
 * <p>提供记录级别 CRUD 与查询封装，隐藏鉴权和响应解析细节。</p>
 */
@Slf4j
public class FsDwRecordService {

    /**
     * 飞书接口统一成功码。
     */
    private static final Integer SUCCESS_CODE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    @Autowired
    private FsDwTokenService fsDwTokenService;

    @Autowired
    private FsDwRecordApi fsDwRecordApi;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 新增记录。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   多维表格数据表的唯一标识
     * @param req       请求参数
     * @return 新增结果
     */
    public AddRecordRes addRecord(String appId, String appSecret, String appToken, String tableId, AddRecordReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]新增记录请求不能为空");
        BitableAssert.notEmpty(req.getFields(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]新增记录字段不能为空");
        validateTableInfo(appToken, tableId);
        String res = fsDwRecordApi.addRecord(buildAuthorization(appId, appSecret), appToken, tableId, req);
        AddRecordRes result = parseResponse("新增记录", res, AddRecordRes.class);
        log.info("[飞书多维表格]新增记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 新增记录。
     */
    public AddRecordRes addRecord(String appId, String appSecret, String appToken, String tableId, Map<String, Object> fields) {
        return addRecord(appId, appSecret, appToken, tableId, FsDwRecordHelper.buildAddRecordReq(fields));
    }


    /**
     * 新增记录。
     */
    public AddRecordRes addRecord(String appId, String appSecret, String appToken, String tableId, Object payload) {
        return addRecord(appId, appSecret, appToken, tableId, FsDwRecordHelper.buildAddRecordReq(payload));
    }

    /**
     * 批量新增记录。
     */
    public BatchCreateRecordRes batchCreateRecord(String appId, String appSecret, String appToken, String tableId, BatchCreateRecordReq req) {
        return batchCreateRecord(appId, appSecret, appToken, tableId, null, null, null, req);
    }

    /**
     * 批量新增记录。
     */
    public BatchCreateRecordRes batchCreateRecord(String appId, String appSecret, String appToken, String tableId,
                                                  String userIdType, String clientToken, Boolean ignoreConsistencyCheck,
                                                  BatchCreateRecordReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量新增记录请求不能为空");
        BitableAssert.notEmpty(req.getRecords(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量新增记录不能为空");
        validateTableInfo(appToken, tableId);
        validateBatchCreateRecords(req);
        String res = fsDwRecordApi.batchCreateRecord(buildAuthorization(appId, appSecret), appToken, tableId,
                userIdType, clientToken, ignoreConsistencyCheck, req);
        BatchCreateRecordRes result = parseResponse("批量新增记录", res, BatchCreateRecordRes.class);
        log.info("[飞书多维表格]批量新增记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 更新记录。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   多维表格数据表的唯一标识
     * @param recordId  多维表格记录的唯一标识
     * @param req       请求参数
     * @return 更新结果
     */
    public UpdateRecordRes updateRecord(String appId, String appSecret, String appToken, String tableId, String recordId, UpdateRecordReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]更新记录请求不能为空");
        BitableAssert.notEmpty(req.getFields(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]更新记录字段不能为空");
        validateTableInfo(appToken, tableId);
        validateRecordId(recordId);
        String res = fsDwRecordApi.updateRecord(buildAuthorization(appId, appSecret), appToken, tableId, recordId, req);
        UpdateRecordRes result = parseResponse("更新记录", res, UpdateRecordRes.class);
        log.info("[飞书多维表格]更新记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 批量更新记录。
     */
    public UpdateRecordRes updateRecord(String appId, String appSecret, String appToken, String tableId, String recordId, Map<String, Object> fields) {
        return updateRecord(appId, appSecret, appToken, tableId, recordId, FsDwRecordHelper.buildUpdateRecordReq(fields));
    }

    /**
     * 批量更新记录。
     */
    public UpdateRecordRes updateRecord(String appId, String appSecret, String appToken, String tableId, String recordId, Object payload) {
        return updateRecord(appId, appSecret, appToken, tableId, recordId, FsDwRecordHelper.buildUpdateRecordReq(payload));
    }

    /**
     * 批量更新记录。
     */
    public BatchUpdateRecordRes batchUpdateRecord(String appId, String appSecret, String appToken, String tableId, BatchUpdateRecordReq req) {
        return batchUpdateRecord(appId, appSecret, appToken, tableId, null, null, req);
    }

    /**
     * 批量更新记录。
     */
    public BatchUpdateRecordRes batchUpdateRecord(String appId, String appSecret, String appToken, String tableId,
                                                  String userIdType, Boolean ignoreConsistencyCheck, BatchUpdateRecordReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量更新记录请求不能为空");
        BitableAssert.notEmpty(req.getRecords(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量更新记录不能为空");
        validateTableInfo(appToken, tableId);
        validateBatchUpdateRecords(req);
        String res = fsDwRecordApi.batchUpdateRecord(buildAuthorization(appId, appSecret), appToken, tableId,
                userIdType, ignoreConsistencyCheck, req);
        BatchUpdateRecordRes result = parseResponse("批量更新记录", res, BatchUpdateRecordRes.class);
        log.info("[飞书多维表格]批量更新记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 查询记录。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   多维表格数据表的唯一标识
     * @param req       请求参数
     * @return 查询结果
     */
    public QueryRecordRes queryRecord(String appId, String appSecret, String appToken, String tableId, QueryRecordReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]查询记录请求不能为空");
        validateTableInfo(appToken, tableId);
        Integer pageNo = req.getPageNo();
        if (pageNo != null) {
            BitableAssert.isTrue(pageNo > 0, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]pageNo必须大于0");
            QueryRecordRes result = queryRecordByPage(appId, appSecret, appToken, tableId, req, pageNo);
            log.info("[飞书多维表格]查询记录成功, code={}", result.getCode());
            return result;
        }
        String pageToken = req.getPageToken();
        Integer pageSize = req.getPageSize();
        String res = executeQueryRecordRequest(appId, appSecret, appToken, tableId, req, pageToken, pageSize);
        QueryRecordRes result = parseResponse("查询记录", res, QueryRecordRes.class);
        log.info("[飞书多维表格]查询记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 查询记录并映射为实体列表。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   多维表格数据表的唯一标识
     * @param req       请求参数
     * @param clazz     实体类型
     * @return 实体列表
     */
    public <T> List<T> queryRecord(String appId, String appSecret, String appToken, String tableId, QueryRecordReq req, Class<T> clazz) {
        FsDwRecordHelper.fillQueryFieldNames(req, clazz);
        QueryRecordRes res = queryRecord(appId, appSecret, appToken, tableId, req);
        return FsDwRecordHelper.toEntityList(res, clazz);
    }

    /**
     * 删除记录。
     *
     * @param appId     应用ID
     * @param appSecret 应用密钥
     * @param appToken  多维表格 App 的唯一标识
     * @param tableId   多维表格数据表的唯一标识
     * @param recordId  记录 ID
     * @return 删除结果
     */
    public DeleteRecordRes deleteRecord(String appId, String appSecret, String appToken, String tableId, String recordId) {
        validateTableInfo(appToken, tableId);
        validateRecordId(recordId);
        String res = fsDwRecordApi.deleteRecord(buildAuthorization(appId, appSecret), appToken, tableId, recordId);
        DeleteRecordRes result = parseResponse("删除记录", res, DeleteRecordRes.class);
        log.info("[飞书多维表格]删除记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 批量删除记录。
     */
    public BatchDeleteRecordRes batchDeleteRecord(String appId, String appSecret, String appToken, String tableId, BatchDeleteRecordReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量删除记录请求不能为空");
        BitableAssert.notEmpty(req.getRecords(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量删除记录不能为空");
        validateTableInfo(appToken, tableId);
        String res = fsDwRecordApi.batchDeleteRecord(buildAuthorization(appId, appSecret), appToken, tableId, req);
        BatchDeleteRecordRes result = parseResponse("批量删除记录", res, BatchDeleteRecordRes.class);
        log.info("[飞书多维表格]批量删除记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 批量获取记录。
     */
    public BatchGetRecordRes batchGetRecord(String appId, String appSecret, String appToken, String tableId, BatchGetRecordReq req) {
        BitableAssert.notNull(req, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量获取记录请求不能为空");
        BitableAssert.notEmpty(req.getRecordIds(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量获取记录不能为空");
        validateTableInfo(appToken, tableId);
        String res = fsDwRecordApi.batchGetRecord(buildAuthorization(appId, appSecret), appToken, tableId, req);
        BatchGetRecordRes result = parseResponse("批量获取记录", res, BatchGetRecordRes.class);
        log.info("[飞书多维表格]批量获取记录成功, code={}", result.getCode());
        return result;
    }

    /**
     * 构建授权信息。
     */
    private String buildAuthorization(String appId, String appSecret) {
        String token = fsDwTokenService.getToken(appId, appSecret);
        BitableAssert.notBlank(token, BitableErrorCode.TOKEN_ACQUIRE_FAILED, "[飞书多维表格]token获取失败");
        return FsDwConstants.AUTHORIZATION_PREFIX + token;
    }

    /**
     * 校验表格信息。
     */
    private <T extends AbstractRes<?>> T parseResponse(String action, String res, Class<T> clazz) {
        BitableAssert.notBlank(res, BitableErrorCode.FEISHU_RESPONSE_EMPTY, "[飞书多维表格][{}]响应为空", action);
        try {
            T result = objectMapper.readValue(res, clazz);
            BitableAssert.notNull(result, BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, "[飞书多维表格][{}]解析结果为空", action);
            BitableAssert.isTrue(SUCCESS_CODE.equals(result.getCode()), BitableErrorCode.FEISHU_API_ERROR,
                    "[飞书多维表格][{}]失败, code={}, msg={}, error={}", action, result.getCode(), result.getMsg(), result.getError());
            return result;
        } catch (BitableException e) {
            throw e;
        } catch (Exception e) {
            String message = StrUtil.format("[飞书多维表格][{}]解析响应失败: {}", action, res);
            throw new BitableException(BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, message, e);
        }
    }

    /**
     * 校验表格信息。
     */
    private void validateTableInfo(String appToken, String tableId) {
        BitableAssert.notBlank(appToken, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]appToken不能为空");
        BitableAssert.notBlank(tableId, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]tableId不能为空");
    }

    /**
     * 校验记录 ID。
     */
    private void validateRecordId(String recordId) {
        BitableAssert.notBlank(recordId, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]recordId不能为空");
    }

    /**
     * 批量创建记录。
     */
    private void validateBatchCreateRecords(BatchCreateRecordReq req) {
        if (req == null || req.getRecords() == null) {
            return;
        }
        for (BatchCreateRecordReq.Record record : req.getRecords()) {
            BitableAssert.notNull(record, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量新增记录项不能为空");
            BitableAssert.notEmpty(record.getFields(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量新增记录字段不能为空");
        }
    }

    /**
     * 批量更新记录。
     */
    private void validateBatchUpdateRecords(BatchUpdateRecordReq req) {
        if (req == null || req.getRecords() == null) {
            return;
        }
        for (BatchUpdateRecordReq.Record record : req.getRecords()) {
            BitableAssert.notNull(record, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量更新记录项不能为空");
            BitableAssert.notBlank(record.getRecordId(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量更新recordId不能为空");
            BitableAssert.notEmpty(record.getFields(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]批量更新记录字段不能为空");
        }
    }

    /**
     * 执行查询记录请求。
     */
    private String executeQueryRecordRequest(String appId, String appSecret, String appToken, String tableId,
                                             QueryRecordReq req, String pageToken, Integer pageSize) {
        HttpUrl baseUrl = HttpUrl.parse(FsDwConstants.OPEN_API_PREFIX);
        BitableAssert.notNull(baseUrl, BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, "[飞书多维表格]查询记录URL构建失败");
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder()
                .addPathSegment("bitable")
                .addPathSegment("v1")
                .addPathSegment("apps")
                .addPathSegment(appToken)
                .addPathSegment("tables")
                .addPathSegment(tableId)
                .addPathSegment("records")
                .addPathSegment("search");
        if (StrUtil.isNotBlank(pageToken)) {
            urlBuilder.addQueryParameter("page_token", pageToken);
        }
        if (pageSize != null) {
            urlBuilder.addQueryParameter("page_size", String.valueOf(pageSize));
        }
        QueryRecordReq bodyReq = buildQueryRecordBody(req);
        String payload;
        try {
            payload = objectMapper.writeValueAsString(bodyReq);
        } catch (Exception e) {
            String message = StrUtil.format("[飞书多维表格][查询记录]序列化请求失败: {}", e.getMessage());
            throw new BitableException(BitableErrorCode.FEISHU_RESPONSE_PARSE_ERROR, message, e);
        }
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, payload);
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Authorization", buildAuthorization(appId, appSecret))
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
            if (response.body() == null) {
                return null;
            }
            return response.body().string();
        } catch (Exception e) {
            String message = StrUtil.format("[飞书多维表格][查询记录]接口请求失败: {}", e.getMessage());
            throw new BitableException(BitableErrorCode.FEISHU_API_ERROR, message, e);
        }
    }

    /**
     * 按页查询记录
     *
     * @param appId     应用 ID
     * @param appSecret 应用密钥
     * @param appToken  应用令牌
     * @param tableId   表格 ID
     * @param req       查询条件
     * @param pageNo    页码
     * @return 查询结果
     */
    private QueryRecordRes queryRecordByPage(String appId, String appSecret, String appToken, String tableId,
                                             QueryRecordReq req, int pageNo) {
        int pageSize = req.getPageSize() == null ? DEFAULT_PAGE_SIZE : req.getPageSize();
        String pageToken = null;
        QueryRecordRes lastRes = null;
        for (int currentPage = 1; currentPage <= pageNo; currentPage++) {
            String res = executeQueryRecordRequest(appId, appSecret, appToken, tableId, req, pageToken, pageSize);
            lastRes = parseResponse("查询记录", res, QueryRecordRes.class);
            if (currentPage == pageNo) {
                return lastRes;
            }
            QueryRecordRes.RecordRes data = lastRes.getData();
            boolean hasMore = data != null && Boolean.TRUE.equals(data.getHasMore());
            pageToken = data == null ? null : data.getPageToken();
            if (!hasMore || StrUtil.isBlank(pageToken)) {
                break;
            }
        }
        return buildEmptyQueryRecordRes(lastRes);
    }

    /**
     * 构建空的查询记录响应
     *
     * @param lastRes 最后一次查询记录响应
     * @return 空的查询记录响应
     */
    private QueryRecordRes buildEmptyQueryRecordRes(QueryRecordRes lastRes) {
        QueryRecordRes empty = new QueryRecordRes();
        empty.setCode(SUCCESS_CODE);
        empty.setMsg(lastRes == null ? "success" : lastRes.getMsg());
        empty.setError(lastRes == null ? null : lastRes.getError());
        QueryRecordRes.RecordRes data = new QueryRecordRes.RecordRes();
        data.setHasMore(false);
        data.setPageToken(null);
        data.setTotal(lastRes == null || lastRes.getData() == null ? null : lastRes.getData().getTotal());
        data.setItems(Collections.emptyList());
        empty.setData(data);
        return empty;
    }

    /**
     * 构建查询记录请求体
     *
     * @param req 查询记录请求参数
     * @return 查询记录请求体
     */
    private QueryRecordReq buildQueryRecordBody(QueryRecordReq req) {
        QueryRecordReq bodyReq = new QueryRecordReq();
        bodyReq.setViewId(req.getViewId());
        bodyReq.setFieldNames(req.getFieldNames());
        bodyReq.setSort(req.getSort());
        bodyReq.setAutomaticFields(req.getAutomaticFields());
        bodyReq.setFilter(req.getFilter());
        return bodyReq;
    }
}
