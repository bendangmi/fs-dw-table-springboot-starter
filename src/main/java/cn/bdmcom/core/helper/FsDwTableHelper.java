package cn.bdmcom.core.helper;

import cn.bdmcom.config.FsDwProperties;
import cn.bdmcom.core.domain.FsDwTable;
import cn.bdmcom.core.domain.req.BatchCreateTableReq;
import cn.bdmcom.core.domain.req.BatchDeleteTableReq;
import cn.bdmcom.core.domain.req.CreateTableReq;
import cn.bdmcom.core.domain.req.UpdateTableReq;
import cn.bdmcom.core.domain.res.*;
import cn.bdmcom.core.service.FsDwTableService;
import cn.bdmcom.support.BitableAssert;
import cn.bdmcom.support.BitableErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 飞书多维表格数据表辅助类。
 */
public final class FsDwTableHelper {

    private FsDwTableHelper() {
    }

    private static volatile FsDwTableService TABLE_SERVICE;
    private static volatile FsDwProperties PROPERTIES;

    public static void registerServices(FsDwTableService tableService) {
        TABLE_SERVICE = tableService;
    }

    public static void registerProperties(FsDwProperties properties) {
        PROPERTIES = properties;
    }

    /**
     * 新增数据表（默认 appId/appSecret/appToken）。
     */
    public static CreateTableRes createTable(CreateTableReq req) {
        return createTable(requireAppId(), requireAppSecret(), requireAppToken(), req);
    }

    /**
     * 新增数据表（基于 appId/appSecret/appToken）。
     */
    public static CreateTableRes createTable(String appId, String appSecret, String appToken, CreateTableReq req) {
        return requireService().createTable(appId, appSecret, appToken, req);
    }

    /**
     * 新增数据表（基于实体注解生成请求）。
     */
    public static CreateTableRes createTable(Class<?> entityClass) {
        return createTable(requireAppId(), requireAppSecret(), requireAppToken(), entityClass);
    }

    /**
     * 新增数据表（基于 appId/appSecret/appToken）。
     */
    public static CreateTableRes createTable(String appId, String appSecret, String appToken, Class<?> entityClass) {
        CreateTableReq req = buildCreateTableReq(entityClass);
        return createTable(appId, appSecret, appToken, req);
    }

    /**
     * 批量新增数据表（基于实体注解生成请求）。
     */
    public static CreateTableReq buildCreateTableReq(Class<?> entityClass) {
        FsDwTable tableMeta = requireTableMeta(entityClass);
        String tableName = tableMeta.name();
        BitableAssert.notBlank(tableName, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]表名称未配置");
        CreateTableReq.Table table = new CreateTableReq.Table();
        table.setName(tableName.trim());
        String defaultViewName = tableMeta.defaultViewName();
        if (defaultViewName != null && !defaultViewName.trim().isEmpty()) {
            table.setDefaultViewName(defaultViewName.trim());
        } else {
            table.setDefaultViewName(tableName);
        }
        List<CreateTableReq.Field> fields = FsDwFieldHelper.buildCreateTableFields(entityClass);
        if (!fields.isEmpty()) {
            table.setFields(fields);
        }
        if (table.getDefaultViewName() != null && fields.isEmpty()) {
            BitableAssert.fail(BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]默认视图名称存在时必须提供字段定义");
        }
        CreateTableReq req = new CreateTableReq();
        req.setTable(table);
        return req;
    }

    /**
     * 批量新增数据表（默认 appId/appSecret/appToken）。
     */
    public static BatchCreateTableRes batchCreateTable(BatchCreateTableReq req) {
        return batchCreateTable(requireAppId(), requireAppSecret(), requireAppToken(), req);
    }

    /**
     * 批量新增数据表（基于 appId/appSecret/appToken）。
     */
    public static BatchCreateTableRes batchCreateTable(String appId, String appSecret, String appToken, BatchCreateTableReq req) {
        return requireService().batchCreateTable(appId, appSecret, appToken, req);
    }

    /**
     * 更新数据表名称（默认 appId/appSecret/appToken）。
     */
    public static UpdateTableRes updateTable(String tableId, UpdateTableReq req) {
        return updateTable(requireAppId(), requireAppSecret(), requireAppToken(), tableId, req);
    }

    /**
     * 更新数据表名称（基于实体注解生成请求）。
     */
    public static UpdateTableRes updateTable(Class<?> entityClass) {
        return updateTable(requireAppId(), requireAppSecret(), requireAppToken(), entityClass);
    }

    /**
     * 批量更新数据表名称（基于实体注解生成请求）。
     */
    public static UpdateTableRes updateTable(Class<?> entityClass, UpdateTableReq req) {
        String tableId = resolveTableId(entityClass);
        return updateTable(requireAppId(), requireAppSecret(), requireAppToken(), tableId, req);
    }

    /**
     * 批量更新数据表名称（基于实体注解生成请求）。
     */
    public static UpdateTableRes updateTable(String appId, String appSecret, String appToken, Class<?> entityClass) {
        String tableId = resolveTableId(entityClass);
        UpdateTableReq req = buildUpdateTableReq(entityClass);
        return updateTable(appId, appSecret, appToken, tableId, req);
    }

    /**
     * 更新数据表名称（基于 appId/appSecret/appToken）。
     */
    public static UpdateTableRes updateTable(String appId, String appSecret, String appToken, String tableId, UpdateTableReq req) {
        return requireService().updateTable(appId, appSecret, appToken, tableId, req);
    }

    /**
     * 删除数据表（默认 appId/appSecret/appToken）。
     */
    public static DeleteTableRes deleteTable(Class<?> entityClass) {
        String tableId = resolveTableId(entityClass);
        return deleteTable(requireAppId(), requireAppSecret(), requireAppToken(), tableId);
    }

    /**
     * 删除数据表（基于 appId/appSecret/appToken）。
     */
    public static DeleteTableRes deleteTable(String tableId) {
        return deleteTable(requireAppId(), requireAppSecret(), requireAppToken(), tableId);
    }

    /**
     * 删除数据表（基于 appId/appSecret/appToken）。
     */
    public static DeleteTableRes deleteTable(String appId, String appSecret, String appToken, String tableId) {
        return requireService().deleteTable(appId, appSecret, appToken, tableId);
    }

    /**
     * 批量删除数据表（默认 appId/appSecret/appToken）。
     */
    public static BatchDeleteTableRes batchDeleteTable(BatchDeleteTableReq req) {
        return batchDeleteTable(requireAppId(), requireAppSecret(), requireAppToken(), req);
    }

    /**
     * 批量删除数据表（基于 appId/appSecret/appToken）。
     */
    public static BatchDeleteTableRes batchDeleteTable(String appId, String appSecret, String appToken, BatchDeleteTableReq req) {
        return requireService().batchDeleteTable(appId, appSecret, appToken, req);
    }

    /**
     * 列出数据表（默认 appId/appSecret/appToken）。
     */
    public static ListTableRes listTables(Integer pageSize, String pageToken) {
        return listTables(requireAppId(), requireAppSecret(), requireAppToken(), pageSize, pageToken);
    }

    /**
     * 列出数据表（基于 appId/appSecret/appToken）。
     */
    public static ListTableRes listTables(String appId, String appSecret, String appToken, Integer pageSize, String pageToken) {
        return requireService().listTables(appId, appSecret, appToken, pageSize, pageToken);
    }

    /**
     * 列出所有数据表（自动分页）。
     */
    public static List<ListTableRes.TableItem> listAllTables() {
        return listAllTables(requireAppToken());
    }

    /**
     * 列出所有数据表（自动分页）。
     */
    public static List<ListTableRes.TableItem> listAllTables(String appToken) {
        BitableAssert.notBlank(appToken, BitableErrorCode.APP_TOKEN_MISSING, "[飞书多维表格]appToken未配置");
        List<ListTableRes.TableItem> result = new ArrayList<>();
        String pageToken = null;
        boolean hasMore;
        do {
            ListTableRes res = listTables(requireAppId(), requireAppSecret(), appToken, 100, pageToken);
            ListTableRes.TableListData data = res.getData();
            if (data != null && data.getItems() != null) {
                result.addAll(data.getItems());
            }
            hasMore = data != null && Boolean.TRUE.equals(data.getHasMore());
            pageToken = data == null ? null : data.getPageToken();
        } while (hasMore && pageToken != null);
        return result;
    }

    /**
     * 获取表服务（默认 appId/appSecret/appToken）。
     */
    private static FsDwTableService requireService() {
        BitableAssert.notNull(TABLE_SERVICE, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]表服务未注册");
        return TABLE_SERVICE;
    }

    /**
     * 获取配置（默认 appId/appSecret/appToken）。
     */
    private static FsDwProperties requireProperties() {
        BitableAssert.notNull(PROPERTIES, BitableErrorCode.TABLE_CONFIG_MISSING, "[飞书多维表格]配置未加载");
        return PROPERTIES;
    }

    /**
     * 获取应用ID（默认 appId/appSecret/appToken）。
     */
    private static String requireAppId() {
        String appId = requireProperties().getAppId();
        BitableAssert.notBlank(appId, BitableErrorCode.APP_CREDENTIALS_MISSING, "[飞书多维表格]appId未配置");
        return appId;
    }

    /**
     * 获取应用密钥（默认 appId/appSecret/appToken）。
     */
    private static String requireAppSecret() {
        String appSecret = requireProperties().getAppSecret();
        BitableAssert.notBlank(appSecret, BitableErrorCode.APP_CREDENTIALS_MISSING, "[飞书多维表格]appSecret未配置");
        return appSecret;
    }

    /**
     * 获取应用令牌（默认 appId/appSecret/appToken）。
     */
    private static String requireAppToken() {
        String appToken = requireProperties().getAppToken();
        BitableAssert.notBlank(appToken, BitableErrorCode.APP_TOKEN_MISSING, "[飞书多维表格]appToken未配置");
        return appToken;
    }

    /**
     * 获取数据表ID（默认 appId/appSecret/appToken）。
     */
    private static String resolveTableId(Class<?> entityClass) {
        BitableAssert.notNull(entityClass, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]实体类型不能为空");
        FsDwTable table = requireTableMeta(entityClass);
        BitableAssert.notBlank(table.tableId(), BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]tableId未配置");
        return table.tableId();
    }

    /**
     * 获取数据表元信息（默认 appId/appSecret/appToken）。
     */
    private static FsDwTable requireTableMeta(Class<?> entityClass) {
        BitableAssert.notNull(entityClass, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]实体类型不能为空");
        FsDwTable table = entityClass.getAnnotation(FsDwTable.class);
        BitableAssert.notNull(table, BitableErrorCode.TABLE_META_MISSING, "[飞书多维表格]实体未标注@FsDwTable");
        return table;
    }

    /**
     * 构建更新数据表请求（默认 appId/appSecret/appToken）。
     */
    private static UpdateTableReq buildUpdateTableReq(Class<?> entityClass) {
        FsDwTable tableMeta = requireTableMeta(entityClass);
        String tableName = tableMeta.name();
        BitableAssert.notBlank(tableName, BitableErrorCode.PARAM_REQUIRED, "[飞书多维表格]表名称未配置");
        UpdateTableReq req = new UpdateTableReq();
        req.setName(tableName.trim());
        return req;
    }
}
