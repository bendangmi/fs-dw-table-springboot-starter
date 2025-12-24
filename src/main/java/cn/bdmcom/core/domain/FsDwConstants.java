package cn.bdmcom.core.domain;

/**
 * 飞书多维表格常量定义。
 */
public final class FsDwConstants {

    private FsDwConstants() {
    }

    /**
     * 飞书开放平台域名。
     */
    public static final String FEISHU_HOST = "https://open.feishu.cn";

    /**
     * 多维表格 API 前缀。
     */
    public static final String OPEN_API_PREFIX = FEISHU_HOST + "/open-apis";

    /**
     * App Access Token 获取接口。
     */
    public static final String APP_ACCESS_TOKEN_URL = OPEN_API_PREFIX + "/auth/v3/app_access_token/internal";

    /**
     * 表格记录操作 URL 模板。
     */
    public static final String RECORDS_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/records";

    /**
     * 记录详情 URL 模板。
     */
    public static final String RECORD_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/records/{record_id}";

    /**
     * 记录查询 URL。
     */
    public static final String RECORDS_SEARCH_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/records/search?page_token={page_token}&page_size={page_size}";

    /**
     * 批量新增记录 URL。
     */
    public static final String RECORDS_BATCH_CREATE_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/records/batch_create";

    /**
     * 批量更新记录 URL。
     */
    public static final String RECORDS_BATCH_UPDATE_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/records/batch_update";

    /**
     * 批量删除记录 URL。
     */
    public static final String RECORDS_BATCH_DELETE_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/records/batch_delete";

    /**
     * 批量获取记录 URL。
     */
    public static final String RECORDS_BATCH_GET_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/records/batch_get";

    /**
     * 字段列表 URL。
     */
    public static final String FIELDS_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/fields";

    /**
     * 字段详情 URL。
     */
    public static final String FIELD_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}/fields/{field_id}";

    /**
     * 数据表列表/新增 URL。
     */
    public static final String TABLES_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables";

    /**
     * 数据表详情 URL。
     */
    public static final String TABLE_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/{table_id}";

    /**
     * 批量新增数据表 URL。
     */
    public static final String TABLES_BATCH_CREATE_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/batch_create";

    /**
     * 批量删除数据表 URL。
     */
    public static final String TABLES_BATCH_DELETE_URL = OPEN_API_PREFIX + "/bitable/v1/apps/{app_token}/tables/batch_delete";

    /**
     * Authorization header 前缀。
     */
    public static final String AUTHORIZATION_PREFIX = "Bearer ";
}
