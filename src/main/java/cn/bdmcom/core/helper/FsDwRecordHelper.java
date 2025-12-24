package cn.bdmcom.core.helper;

import cn.bdmcom.config.FsDwProperties;
import cn.bdmcom.core.domain.DwLambdaQueryWrapper;
import cn.bdmcom.core.domain.FsDwTable;
import cn.bdmcom.core.domain.FsDwTableId;
import cn.bdmcom.core.domain.FsDwTableProperty;
import cn.bdmcom.core.domain.req.*;
import cn.bdmcom.core.domain.res.*;
import cn.bdmcom.core.service.FsDwRecordService;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;
import cn.bdmcom.support.BitableException;
import cn.hutool.core.util.StrUtil;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 飞书多维表格记录辅助类。
 */
public final class FsDwRecordHelper {

    private FsDwRecordHelper() {
    }

    private static volatile FsDwRecordService RECORD_SERVICE;
    private static volatile FsDwProperties PROPERTIES;

    public static void registerServices(FsDwRecordService recordService) {
        RECORD_SERVICE = recordService;
    }

    public static void registerProperties(FsDwProperties properties) {
        PROPERTIES = properties;
    }

    public static AddRecordRes addRecord(String appId, String appSecret, String appToken, String tableId, Map<String, Object> fields) {
        return requireService().addRecord(appId, appSecret, appToken, tableId, fields);
    }

    public static AddRecordRes addRecord(String appId, String appSecret, String appToken, String tableId, Object payload) {
        return requireService().addRecord(appId, appSecret, appToken, tableId, payload);
    }

    public static AddRecordRes addRecord(String tableId, Map<String, Object> fields) {
        return addRecord(requireAppId(), requireAppSecret(), requireAppToken(), tableId, fields);
    }

    public static AddRecordRes addRecord(String tableId, Object payload) {
        return addRecord(requireAppId(), requireAppSecret(), requireAppToken(), tableId, payload);
    }

    /**
     * 新增记录（从实体注解读取 tableId）。
     */
    public static AddRecordRes addRecord(Object payload) {
        TableMeta meta = resolveTableMeta(payload);
        return addRecord(requireAppId(), requireAppSecret(), requireAppToken(), meta.getTableId(), payload);
    }

    /**
     * 批量新增记录（使用默认 appId/appSecret/appToken）。
     */
    public static BatchCreateRecordRes batchCreateRecords(String tableId, BatchCreateRecordReq req) {
        return batchCreateRecords(requireAppId(), requireAppSecret(), requireAppToken(), tableId, null, null, null, req);
    }

    public static BatchCreateRecordRes batchCreateRecords(String tableId, String userIdType, String clientToken,
                                                          Boolean ignoreConsistencyCheck, BatchCreateRecordReq req) {
        return batchCreateRecords(requireAppId(), requireAppSecret(), requireAppToken(), tableId, userIdType, clientToken,
                ignoreConsistencyCheck, req);
    }

    public static BatchCreateRecordRes batchCreateRecords(String tableId, List<?> payloads) {
        return batchCreateRecords(tableId, buildBatchCreateRecordReq(payloads));
    }

    public static BatchCreateRecordRes batchCreateRecords(Class<?> entityClass, List<?> payloads) {
        String tableId = resolveTableMeta(entityClass).getTableId();
        return batchCreateRecords(tableId, payloads);
    }

    public static BatchCreateRecordRes batchCreateRecords(String appId, String appSecret, String appToken, String tableId,
                                                          String userIdType, String clientToken, Boolean ignoreConsistencyCheck,
                                                          BatchCreateRecordReq req) {
        return requireService().batchCreateRecord(appId, appSecret, appToken, tableId, userIdType, clientToken,
                ignoreConsistencyCheck, req);
    }

    public static UpdateRecordRes updateRecord(String appId, String appSecret, String appToken, String tableId, String recordId, Map<String, Object> fields) {
        return requireService().updateRecord(appId, appSecret, appToken, tableId, recordId, fields);
    }

    public static UpdateRecordRes updateRecord(String appId, String appSecret, String appToken, String tableId, String recordId, Object payload) {
        return requireService().updateRecord(appId, appSecret, appToken, tableId, recordId, payload);
    }

    public static UpdateRecordRes updateRecord(String tableId, String recordId, Map<String, Object> fields) {
        return updateRecord(requireAppId(), requireAppSecret(), requireAppToken(), tableId, recordId, fields);
    }

    public static UpdateRecordRes updateRecord(String tableId, String recordId, Object payload) {
        return updateRecord(requireAppId(), requireAppSecret(), requireAppToken(), tableId, recordId, payload);
    }

    /**
     * 更新记录（recordId 来自实体注解）。
     */
    public static UpdateRecordRes updateRecord(Object payload) {
        TableMeta meta = resolveTableMeta(payload);
        String recordId = resolveRecordId(payload);
        return updateRecord(requireAppId(), requireAppSecret(), requireAppToken(), meta.getTableId(), recordId, payload);
    }

    /**
     * 批量更新记录（使用默认 appId/appSecret/appToken）。
     */
    public static BatchUpdateRecordRes batchUpdateRecords(String tableId, BatchUpdateRecordReq req) {
        return batchUpdateRecords(requireAppId(), requireAppSecret(), requireAppToken(), tableId, null, null, req);
    }

    public static BatchUpdateRecordRes batchUpdateRecords(String tableId, String userIdType, Boolean ignoreConsistencyCheck,
                                                          BatchUpdateRecordReq req) {
        return batchUpdateRecords(requireAppId(), requireAppSecret(), requireAppToken(), tableId, userIdType,
                ignoreConsistencyCheck, req);
    }

    public static BatchUpdateRecordRes batchUpdateRecords(String tableId, List<?> payloads) {
        return batchUpdateRecords(tableId, buildBatchUpdateRecordReq(payloads));
    }

    public static BatchUpdateRecordRes batchUpdateRecords(Class<?> entityClass, List<?> payloads) {
        String tableId = resolveTableMeta(entityClass).getTableId();
        return batchUpdateRecords(tableId, payloads);
    }

    public static BatchUpdateRecordRes batchUpdateRecords(String appId, String appSecret, String appToken, String tableId,
                                                          String userIdType, Boolean ignoreConsistencyCheck, BatchUpdateRecordReq req) {
        return requireService().batchUpdateRecord(appId, appSecret, appToken, tableId, userIdType, ignoreConsistencyCheck, req);
    }

    public static QueryRecordRes queryRecord(String appId, String appSecret, String appToken, String tableId, QueryRecordReq req) {
        return requireService().queryRecord(appId, appSecret, appToken, tableId, req);
    }

    public static QueryRecordRes queryRecord(String tableId, QueryRecordReq req) {
        return queryRecord(requireAppId(), requireAppSecret(), requireAppToken(), tableId, req);
    }

    /**
     * 条件分页查询记录（使用查询构造器）。
     */
    public static QueryRecordRes queryRecord(String tableId, DwLambdaQueryWrapper<?> wrapper) {
        QueryRecordReq req = buildQueryRecordReq(wrapper, null);
        return queryRecord(requireAppId(), requireAppSecret(), requireAppToken(), tableId, req);
    }

    /**
     * 条件分页查询记录（基于实体注解）。
     */
    public static <T> QueryRecordRes queryRecord(Class<T> clazz, DwLambdaQueryWrapper<T> wrapper) {
        QueryRecordReq req = buildQueryRecordReq(wrapper, clazz);
        TableMeta meta = resolveTableMeta(clazz);
        return queryRecord(requireAppId(), requireAppSecret(), requireAppToken(), meta.getTableId(), req);
    }

    public static <T> List<T> queryRecords(String appId, String appSecret, String appToken, String tableId, QueryRecordReq req, Class<T> clazz) {
        return requireService().queryRecord(appId, appSecret, appToken, tableId, req, clazz);
    }

    public static <T> List<T> queryRecords(String tableId, QueryRecordReq req, Class<T> clazz) {
        return queryRecords(requireAppId(), requireAppSecret(), requireAppToken(), tableId, req, clazz);
    }

    /**
     * 条件分页查询记录并映射为实体列表（基于实体注解）。
     */
    public static <T> List<T> queryRecords(Class<T> clazz, DwLambdaQueryWrapper<T> wrapper) {
        QueryRecordReq req = buildQueryRecordReq(wrapper, clazz);
        TableMeta meta = resolveTableMeta(clazz);
        return queryRecords(requireAppId(), requireAppSecret(), requireAppToken(), meta.getTableId(), req, clazz);
    }

    public static <T> List<T> queryRecords(String tableId, String viewId, Class<T> clazz) {
        QueryRecordReq req = buildQueryRecordReq(viewId, clazz);
        return queryRecords(tableId, req, clazz);
    }

    /**
     * 查询记录（使用实体注解中的 tableId/viewId）。
     */
    public static <T> List<T> queryRecords(Class<T> clazz) {
        TableMeta meta = resolveTableMeta(clazz);
        BitableAssert.notBlank(meta.getViewId(), BitableErrorCode.VIEW_ID_MISSING, "[飞书多维表格]viewId未配置");
        QueryRecordReq req = buildQueryRecordReq(meta.getViewId(), clazz);
        return queryRecords(meta.getTableId(), req, clazz);
    }

    public static <T> List<T> queryRecords(QueryRecordReq req, Class<T> clazz) {
        TableMeta meta = resolveTableMeta(clazz);
        if (req != null && StrUtil.isBlank(req.getViewId()) && StrUtil.isNotBlank(meta.getViewId())) {
            req.setViewId(meta.getViewId());
        }
        return queryRecords(meta.getTableId(), req, clazz);
    }

    public static DeleteRecordRes deleteRecord(String appId, String appSecret, String appToken, String tableId, String recordId) {
        return requireService().deleteRecord(appId, appSecret, appToken, tableId, recordId);
    }

    public static DeleteRecordRes deleteRecord(String tableId, String recordId) {
        return deleteRecord(requireAppId(), requireAppSecret(), requireAppToken(), tableId, recordId);
    }

    /**
     * 删除记录（recordId/tableId 由实体注解与字段提供）。
     */
    public static DeleteRecordRes deleteRecord(Object payload) {
        TableMeta meta = resolveTableMeta(payload);
        String recordId = resolveRecordId(payload);
        return deleteRecord(meta.getTableId(), recordId);
    }

    /**
     * 批量删除记录（使用默认 appId/appSecret/appToken）。
     */
    public static BatchDeleteRecordRes batchDeleteRecords(String tableId, BatchDeleteRecordReq req) {
        return batchDeleteRecords(requireAppId(), requireAppSecret(), requireAppToken(), tableId, req);
    }

    public static BatchDeleteRecordRes batchDeleteRecords(String tableId, List<String> recordIds) {
        return batchDeleteRecords(tableId, buildBatchDeleteRecordReq(recordIds));
    }

    public static BatchDeleteRecordRes batchDeleteRecords(Class<?> entityClass, List<String> recordIds) {
        String tableId = resolveTableMeta(entityClass).getTableId();
        return batchDeleteRecords(tableId, recordIds);
    }

    public static BatchDeleteRecordRes batchDeleteRecords(String appId, String appSecret, String appToken, String tableId, BatchDeleteRecordReq req) {
        return requireService().batchDeleteRecord(appId, appSecret, appToken, tableId, req);
    }

    /**
     * 批量获取记录（使用默认 appId/appSecret/appToken）。
     */
    public static BatchGetRecordRes batchGetRecords(String tableId, BatchGetRecordReq req) {
        return batchGetRecords(requireAppId(), requireAppSecret(), requireAppToken(), tableId, req);
    }

    public static BatchGetRecordRes batchGetRecords(String tableId, List<String> recordIds) {
        return batchGetRecords(tableId, buildBatchGetRecordReq(recordIds));
    }

    public static BatchGetRecordRes batchGetRecords(Class<?> entityClass, List<String> recordIds) {
        String tableId = resolveTableMeta(entityClass).getTableId();
        return batchGetRecords(tableId, recordIds);
    }

    public static BatchGetRecordRes batchGetRecords(String appId, String appSecret, String appToken, String tableId, BatchGetRecordReq req) {
        return requireService().batchGetRecord(appId, appSecret, appToken, tableId, req);
    }

    public static AddRecordReq buildAddRecordReq(Map<String, Object> fields) {
        BitableAssert.notNull(fields, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]字段不能为空");
        AddRecordReq req = new AddRecordReq();
        req.setFields(fields);
        return req;
    }

    public static AddRecordReq buildAddRecordReq(Object payload) {
        return buildAddRecordReq(toFields(payload));
    }

    public static BatchCreateRecordReq buildBatchCreateRecordReq(List<?> payloads) {
        BitableAssert.notEmpty(payloads, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]记录列表不能为空");
        List<BatchCreateRecordReq.Record> records = new ArrayList<>();
        for (Object payload : payloads) {
            if (payload instanceof BatchCreateRecordReq.Record) {
                records.add((BatchCreateRecordReq.Record) payload);
                continue;
            }
            BatchCreateRecordReq.Record record = new BatchCreateRecordReq.Record();
            record.setFields(resolveFields(payload));
            records.add(record);
        }
        BatchCreateRecordReq req = new BatchCreateRecordReq();
        req.setRecords(records);
        return req;
    }

    public static UpdateRecordReq buildUpdateRecordReq(Map<String, Object> fields) {
        BitableAssert.notNull(fields, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]字段不能为空");
        UpdateRecordReq req = new UpdateRecordReq();
        req.setFields(fields);
        return req;
    }

    public static UpdateRecordReq buildUpdateRecordReq(Object payload) {
        return buildUpdateRecordReq(toFields(payload));
    }

    public static BatchUpdateRecordReq buildBatchUpdateRecordReq(List<?> payloads) {
        BitableAssert.notEmpty(payloads, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]记录列表不能为空");
        List<BatchUpdateRecordReq.Record> records = new ArrayList<>();
        for (Object payload : payloads) {
            BatchUpdateRecordReq.Record record = new BatchUpdateRecordReq.Record();
            if (payload instanceof BatchUpdateRecordReq.Record) {
                records.add((BatchUpdateRecordReq.Record) payload);
                continue;
            }
            if (payload instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = new LinkedHashMap<>((Map<String, Object>) payload);
                String recordId = resolveRecordId(map);
                BitableAssert.notBlank(recordId, BitableErrorCode.RECORD_ID_MISSING, "[飞书多维表格]recordId未配置");
                map.remove("record_id");
                map.remove("recordId");
                record.setRecordId(recordId);
                record.setFields(map);
                records.add(record);
                continue;
            }
            record.setRecordId(resolveRecordId(payload));
            record.setFields(toFields(payload));
            records.add(record);
        }
        BatchUpdateRecordReq req = new BatchUpdateRecordReq();
        req.setRecords(records);
        return req;
    }

    public static BatchDeleteRecordReq buildBatchDeleteRecordReq(List<String> recordIds) {
        BitableAssert.notEmpty(recordIds, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]记录ID不能为空");
        BatchDeleteRecordReq req = new BatchDeleteRecordReq();
        req.setRecords(recordIds);
        return req;
    }

    public static BatchGetRecordReq buildBatchGetRecordReq(List<String> recordIds) {
        BitableAssert.notEmpty(recordIds, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]记录ID不能为空");
        BatchGetRecordReq req = new BatchGetRecordReq();
        req.setRecordIds(recordIds);
        return req;
    }

    public static QueryRecordReq buildQueryRecordReq(String viewId, Class<?> clazz) {
        QueryRecordReq req = new QueryRecordReq();
        if (StrUtil.isNotBlank(viewId)) {
            req.setViewId(viewId);
        }
        fillQueryFieldNames(req, clazz);
        return req;
    }

    public static QueryRecordReq buildQueryRecordReq(DwLambdaQueryWrapper<?> wrapper, Class<?> clazz) {
        QueryRecordReq req = wrapper == null ? new QueryRecordReq() : wrapper.toQueryRecordReq();
        if (clazz != null) {
            TableMeta meta = resolveTableMeta(clazz);
            if (StrUtil.isBlank(req.getViewId()) && StrUtil.isNotBlank(meta.getViewId())) {
                req.setViewId(meta.getViewId());
            }
            if (req.getFieldNames() == null || req.getFieldNames().isEmpty()) {
                fillQueryFieldNames(req, clazz);
            }
        }
        return req;
    }

    public static void fillQueryFieldNames(QueryRecordReq req, Class<?> clazz) {
        if (req == null) {
            return;
        }
        List<String> fieldNames = resolveFieldNames(clazz);
        if (!fieldNames.isEmpty()) {
            req.setFieldNames(fieldNames);
        }
        if (clazz == null) {
            return;
        }
        Map<String, String> fieldNameMap = buildFieldNameMap(clazz);
        mapSortFieldNames(req.getSort(), fieldNameMap);
        mapFilterFieldNames(req.getFilter(), fieldNameMap);
    }

    private static void mapSortFieldNames(List<QueryRecordReq.Sort> sorts, Map<String, String> fieldNameMap) {
        if (sorts == null || sorts.isEmpty() || fieldNameMap == null || fieldNameMap.isEmpty()) {
            return;
        }
        for (QueryRecordReq.Sort sort : sorts) {
            if (sort == null) {
                continue;
            }
            String mapped = resolveMappedFieldName(fieldNameMap, sort.getFieldName());
            sort.setFieldName(mapped);
        }
    }

    private static void mapFilterFieldNames(QueryRecordReq.Filter filter, Map<String, String> fieldNameMap) {
        if (filter == null || fieldNameMap == null || fieldNameMap.isEmpty()) {
            return;
        }
        List<QueryRecordReq.Filter.Condition> conditions = filter.getConditions();
        if (conditions != null) {
            for (QueryRecordReq.Filter.Condition condition : conditions) {
                if (condition == null) {
                    continue;
                }
                String mapped = resolveMappedFieldName(fieldNameMap, condition.getFieldName());
                condition.setFieldName(mapped);
            }
        }
        List<QueryRecordReq.Filter> children = filter.getChildren();
        if (children != null) {
            for (QueryRecordReq.Filter child : children) {
                mapFilterFieldNames(child, fieldNameMap);
            }
        }
    }

    private static String resolveMappedFieldName(Map<String, String> fieldNameMap, String fieldName) {
        if (StrUtil.isBlank(fieldName)) {
            return fieldName;
        }
        String mapped = fieldNameMap.get(fieldName);
        return mapped == null ? fieldName : mapped;
    }

    private static Map<String, String> buildFieldNameMap(Class<?> clazz) {
        Map<String, String> map = new LinkedHashMap<>();
        if (clazz == null) {
            return map;
        }
        List<Field> fields = getAllFields(clazz);
        for (Field field : fields) {
            if (isTableIdField(field)) {
                map.put(field.getName(), "record_id");
                continue;
            }
            TablePropertyInfo property = resolveTablePropertyInfo(field);
            String resolvedName = resolveFieldName(field, property);
            map.put(field.getName(), resolvedName);
        }
        map.put("recordId", "record_id");
        map.put("record_id", "record_id");
        map.put("createdTime", "created_time");
        map.put("created_time", "created_time");
        map.put("lastModifiedTime", "last_modified_time");
        map.put("last_modified_time", "last_modified_time");
        return map;
    }

    public static List<String> resolveFieldNames(Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        List<Field> fields = getAllFields(clazz);
        boolean hasTableProperty = fields.stream().anyMatch(field -> resolveTablePropertyInfo(field) != null);
        if (!hasTableProperty) {
            return Collections.emptyList();
        }
        List<TablePropertyInfo> properties = new ArrayList<>();
        for (Field field : fields) {
            TablePropertyInfo property = resolveTablePropertyInfo(field);
            if (property == null) {
                continue;
            }
            properties.add(property);
        }
        properties.sort(Comparator.comparingInt(TablePropertyInfo::getOrder));
        List<String> fieldNames = new ArrayList<>();
        for (TablePropertyInfo property : properties) {
            String fieldName = resolveFieldName(property);
            if (StrUtil.isNotBlank(fieldName)) {
                fieldNames.add(fieldName);
            }
        }
        return fieldNames;
    }

    public static <T> List<T> toEntityList(QueryRecordRes res, Class<T> clazz) {
        if (res == null || res.getData() == null || res.getData().getItems() == null) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (QueryRecordRes.RecordRes.Item item : res.getData().getItems()) {
            T entity = toEntity(item, clazz);
            if (entity != null) {
                result.add(entity);
            }
        }
        return result;
    }

    /**
     * 将实体对象转换为多维表格字段 Map。
     */
    public static Map<String, Object> toFields(Object payload) {
        BitableAssert.notNull(payload, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]对象不能为空");
        if (payload instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) payload;
            return new LinkedHashMap<>(map);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        List<Field> fields = getAllFields(payload.getClass());
        boolean hasTableProperty = fields.stream().anyMatch(field -> resolveTablePropertyInfo(field) != null);
        for (Field field : fields) {
            if (isTableIdField(field)) {
                continue;
            }
            TablePropertyInfo property = resolveTablePropertyInfo(field);
            if (hasTableProperty && property == null) {
                continue;
            }
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(payload);
            } catch (IllegalAccessException ignored) {
                continue;
            }
            if (value == null) {
                continue;
            }
            String fieldName = resolveFieldName(field, property);
            result.put(fieldName, normalizeValue(value));
        }
        return result;
    }

    /**
     * 构建单条件过滤器（等于）。
     */
    public static QueryRecordReq.Filter buildEqualsFilter(String fieldName, Object value) {
        BitableAssert.notBlank(fieldName, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]过滤字段不能为空");
        QueryRecordReq.Filter filter = new QueryRecordReq.Filter();
        filter.setConjunction("and");
        QueryRecordReq.Filter.Condition condition = new QueryRecordReq.Filter.Condition();
        condition.setFieldName(fieldName);
        condition.setOperator("is");
        condition.setValue(value);
        List<QueryRecordReq.Filter.Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        filter.setConditions(conditions);
        return filter;
    }

    /**
     * 获取实体中的记录ID。
     */
    public static String resolveRecordId(Object payload) {
        BitableAssert.notNull(payload, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]对象不能为空");
        if (payload instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) payload;
            String recordId = resolveRecordId(map);
            if (StrUtil.isNotBlank(recordId)) {
                return recordId;
            }
        }
        List<Field> fields = getAllFields(payload.getClass());
        for (Field field : fields) {
            if (!isTableIdField(field)) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(payload);
                if (value != null) {
                    return String.valueOf(value);
                }
            } catch (IllegalAccessException ignored) {
                // ignore
            }
        }
        for (Field field : fields) {
            if (!isMetaFieldName(field.getName())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(payload);
                if (value != null) {
                    return String.valueOf(value);
                }
            } catch (IllegalAccessException ignored) {
                // ignore
            }
        }
        BitableAssert.fail(BitableErrorCode.RECORD_ID_MISSING, "[飞书多维表格]recordId未配置");
        return null;
    }

    private static Map<String, Object> resolveFields(Object payload) {
        BitableAssert.notNull(payload, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]对象不能为空");
        if (payload instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) payload;
            return new LinkedHashMap<>(map);
        }
        return toFields(payload);
    }

    private static String resolveRecordId(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Object value = map.get("record_id");
        if (value == null) {
            value = map.get("recordId");
        }
        return value == null ? null : String.valueOf(value);
    }

    private static FsDwRecordService requireService() {
        BitableAssert.notNull(RECORD_SERVICE, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]记录服务未注册");
        return RECORD_SERVICE;
    }

    private static FsDwProperties requireProperties() {
        BitableAssert.notNull(PROPERTIES, BitableErrorCode.TABLE_CONFIG_MISSING, "[飞书多维表格]配置未加载");
        return PROPERTIES;
    }

    private static String requireAppId() {
        String appId = requireProperties().getAppId();
        BitableAssert.notBlank(appId, BitableErrorCode.APP_CREDENTIALS_MISSING, "[飞书多维表格]appId未配置");
        return appId;
    }

    private static String requireAppSecret() {
        String appSecret = requireProperties().getAppSecret();
        BitableAssert.notBlank(appSecret, BitableErrorCode.APP_CREDENTIALS_MISSING, "[飞书多维表格]appSecret未配置");
        return appSecret;
    }

    private static String requireAppToken() {
        String appToken = requireProperties().getAppToken();
        BitableAssert.notBlank(appToken, BitableErrorCode.APP_TOKEN_MISSING, "[飞书多维表格]appToken未配置");
        return appToken;
    }

    private static TableMeta resolveTableMeta(Object payload) {
        BitableAssert.notNull(payload, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]对象不能为空");
        return resolveTableMeta(payload.getClass());
    }

    private static TableMeta resolveTableMeta(Class<?> clazz) {
        BitableAssert.notNull(clazz, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]实体类型不能为空");
        FsDwTable table = clazz.getAnnotation(FsDwTable.class);
        BitableAssert.notNull(table, BitableErrorCode.TABLE_META_MISSING, "[飞书多维表格]实体未标注@FsDwTable");
        BitableAssert.notBlank(table.tableId(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]tableId未配置");
        return new TableMeta(table.tableId(), table.viewId());
    }

    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            Field[] declaredFields = current.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isSynthetic() || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    /**
     * 单条记录映射为实体。
     */
    private static <T> T toEntity(QueryRecordRes.RecordRes.Item item, Class<T> clazz) {
        BitableAssert.notNull(clazz, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]实体类型不能为空");
        if (item == null) {
            return null;
        }
        T instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BitableException(BitableErrorCode.ENTITY_CONSTRUCT_FAILED,
                    "[飞书多维表格]实体必须提供无参构造: " + clazz.getName(), e);
        }
        Map<String, Object> fieldsMap = item.getFields() == null ? new LinkedHashMap<>() : item.getFields();

        List<Field> fields = getAllFields(clazz);
        boolean hasTableProperty = fields.stream().anyMatch(field -> resolveTablePropertyInfo(field) != null);
        for (Field field : fields) {
            TablePropertyInfo property = resolveTablePropertyInfo(field);
            boolean metaField = isMetaFieldName(field.getName());
            boolean tableIdField = isTableIdField(field);
            if (tableIdField) {
                Object rawValue = item.getRecordId();
                Object converted = convertValue(rawValue, field.getType());
                try {
                    field.setAccessible(true);
                    if (converted != null || !field.getType().isPrimitive()) {
                        field.set(instance, converted);
                    }
                } catch (Exception ignored) {
                    // ignore incompatible assignments
                }
                continue;
            }
            if (hasTableProperty && property == null && !metaField) {
                continue;
            }
            String fieldName = resolveFieldName(field, property);
            Object rawValue = fieldsMap.get(fieldName);
            if (rawValue == null) {
                rawValue = resolveMetaField(item, fieldName);
            }
            Object converted = convertValue(rawValue, field.getType());
            try {
                field.setAccessible(true);
                if (converted != null || !field.getType().isPrimitive()) {
                    field.set(instance, converted);
                }
            } catch (Exception ignored) {
                // ignore incompatible assignments
            }
        }
        return instance;
    }

    private static boolean isMetaFieldName(String fieldName) {
        if (StrUtil.isBlank(fieldName)) {
            return false;
        }
        return "record_id".equalsIgnoreCase(fieldName)
                || "recordId".equalsIgnoreCase(fieldName)
                || "created_time".equalsIgnoreCase(fieldName)
                || "createdTime".equalsIgnoreCase(fieldName)
                || "last_modified_time".equalsIgnoreCase(fieldName)
                || "lastModifiedTime".equalsIgnoreCase(fieldName);
    }

    /**
     * 读取记录元字段（record_id / created_time / last_modified_time）。
     */
    private static Object resolveMetaField(QueryRecordRes.RecordRes.Item item, String fieldName) {
        if (item == null || StrUtil.isBlank(fieldName)) {
            return null;
        }
        if ("record_id".equalsIgnoreCase(fieldName) || "recordId".equalsIgnoreCase(fieldName)) {
            return item.getRecordId();
        }
        if ("created_time".equalsIgnoreCase(fieldName) || "createdTime".equalsIgnoreCase(fieldName)) {
            return item.getCreatedTime();
        }
        if ("last_modified_time".equalsIgnoreCase(fieldName) || "lastModifiedTime".equalsIgnoreCase(fieldName)) {
            return item.getLastModifiedTime();
        }
        return null;
    }

    /**
     * 类型转换（尽量保持与常用 Java 类型兼容）。
     */
    private static Object convertValue(Object rawValue, Class<?> targetType) {
        if (rawValue == null || targetType == null) {
            return null;
        }
        if (targetType.isInstance(rawValue)) {
            return rawValue;
        }
        if (targetType == String.class) {
            return extractTextValue(rawValue);
        }
        Object scalarValue = rawValue;
        if (!Collection.class.isAssignableFrom(targetType) && !Map.class.isAssignableFrom(targetType)) {
            scalarValue = extractScalarValue(rawValue);
        }
        if (targetType.isInstance(scalarValue)) {
            return scalarValue;
        }
        String rawString = String.valueOf(scalarValue).trim();
        if (targetType == Integer.class || targetType == int.class) {
            return rawString.isEmpty() ? null : Integer.valueOf(rawString);
        }
        if (targetType == Long.class || targetType == long.class) {
            return rawString.isEmpty() ? null : Long.valueOf(rawString);
        }
        if (targetType == Double.class || targetType == double.class) {
            return rawString.isEmpty() ? null : Double.valueOf(rawString);
        }
        if (targetType == Float.class || targetType == float.class) {
            return rawString.isEmpty() ? null : Float.valueOf(rawString);
        }
        if (targetType == BigDecimal.class) {
            return rawString.isEmpty() ? null : new BigDecimal(rawString);
        }
        if (targetType == BigInteger.class) {
            return rawString.isEmpty() ? null : new BigInteger(rawString);
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            if (scalarValue instanceof Boolean) {
                return scalarValue;
            }
            return "1".equals(rawString) || "true".equalsIgnoreCase(rawString);
        }
        if (targetType == LocalDateTime.class) {
            return toLocalDateTime(scalarValue);
        }
        if (targetType == LocalDate.class) {
            LocalDateTime time = toLocalDateTime(scalarValue);
            return time == null ? null : time.toLocalDate();
        }
        if (targetType.isEnum()) {
            @SuppressWarnings("unchecked")
            Class<? extends Enum> enumType = (Class<? extends Enum>) targetType;
            return Enum.valueOf(enumType, rawString);
        }
        if (Collection.class.isAssignableFrom(targetType) && rawValue instanceof Collection) {
            return rawValue;
        }
        if (Map.class.isAssignableFrom(targetType) && rawValue instanceof Map) {
            return rawValue;
        }
        return rawValue;
    }

    /**
     * 从多维表格字段值中提取字符串（支持列表/对象形式）。
     */
    private static String extractTextValue(Object rawValue) {
        if (rawValue == null) {
            return null;
        }
        if (rawValue instanceof String) {
            return (String) rawValue;
        }
        if (rawValue instanceof Map) {
            Object value = extractFromMap((Map<?, ?>) rawValue);
            return value == null ? null : String.valueOf(value);
        }
        if (rawValue instanceof Collection) {
            List<String> values = new ArrayList<>();
            for (Object item : (Collection<?>) rawValue) {
                Object extracted = extractScalarValue(item);
                if (extracted != null) {
                    values.add(String.valueOf(extracted));
                }
            }
            if (values.isEmpty()) {
                return null;
            }
            return values.size() == 1 ? values.get(0) : String.join(",", values);
        }
        return String.valueOf(rawValue);
    }

    private static Object extractScalarValue(Object rawValue) {
        if (rawValue == null) {
            return null;
        }
        if (rawValue instanceof Map) {
            return extractFromMap((Map<?, ?>) rawValue);
        }
        if (rawValue instanceof Collection) {
            for (Object item : (Collection<?>) rawValue) {
                Object extracted = extractScalarValue(item);
                if (extracted != null) {
                    return extracted;
                }
            }
        }
        return rawValue;
    }

    private static Object extractFromMap(Map<?, ?> map) {
        if (map == null) {
            return null;
        }
        Object value = map.get("text");
        if (value != null) {
            return value;
        }
        value = map.get("name");
        if (value != null) {
            return value;
        }
        value = map.get("id");
        if (value != null) {
            return value;
        }
        return map.values().stream().findFirst().orElse(null);
    }

    private static LocalDateTime toLocalDateTime(Object rawValue) {
        if (rawValue == null) {
            return null;
        }
        if (rawValue instanceof Number) {
            long millis = ((Number) rawValue).longValue();
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        String rawString = String.valueOf(rawValue).trim();
        if (rawString.matches("\\d+")) {
            long millis = Long.parseLong(rawString);
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return null;
    }

    /**
     * 解析字段映射名称。
     */
    private static String resolveFieldName(Field field, TablePropertyInfo property) {
        if (property != null) {
            if (StrUtil.isNotBlank(property.getField())) {
                return property.getField();
            }
            String[] values = property.getValue();
            if (values != null && values.length > 0 && StrUtil.isNotBlank(values[0])) {
                return values[0];
            }
        }
        return field.getName();
    }

    private static String resolveFieldName(TablePropertyInfo property) {
        if (property == null) {
            return null;
        }
        if (StrUtil.isNotBlank(property.getField())) {
            return property.getField();
        }
        String[] values = property.getValue();
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    private static TablePropertyInfo resolveTablePropertyInfo(Field field) {
        FsDwTableProperty property = field.getAnnotation(FsDwTableProperty.class);
        if (property != null) {
            return new TablePropertyInfo(property.value(), property.field(), property.order());
        }
        return null;
    }

    private static boolean isTableIdField(Field field) {
        return field.getAnnotation(FsDwTableId.class) != null;
    }

    /**
     * 将字段值规范化为飞书多维表格可接受的格式。
     */
    private static Object normalizeValue(Object value) {
        if (value instanceof LocalDateTime) {
            return toEpochMillis((LocalDateTime) value);
        }
        if (value instanceof LocalDate) {
            return toEpochMillis(((LocalDate) value).atStartOfDay());
        }
        if (value instanceof Date) {
            return ((Date) value).getTime();
        }
        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }
        if (value instanceof Collection) {
            return value;
        }
        if (value.getClass().isArray()) {
            return value;
        }
        return value;
    }

    /**
     * LocalDateTime 转毫秒时间戳。
     */
    private static long toEpochMillis(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static final class TablePropertyInfo {
        private final String[] value;
        private final String field;
        private final int order;

        private TablePropertyInfo(String[] value, String field, int order) {
            this.value = value;
            this.field = field;
            this.order = order;
        }

        private String[] getValue() {
            return value;
        }

        private String getField() {
            return field;
        }

        private int getOrder() {
            return order;
        }
    }

    private static final class TableMeta {
        private final String tableId;
        private final String viewId;

        private TableMeta(String tableId, String viewId) {
            this.tableId = tableId;
            this.viewId = viewId;
        }

        private String getTableId() {
            return tableId;
        }

        private String getViewId() {
            return viewId;
        }
    }
}
