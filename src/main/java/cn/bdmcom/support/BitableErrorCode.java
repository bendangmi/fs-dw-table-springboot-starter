package cn.bdmcom.support;


/**
 * 飞书多维表格错误码。
 *
 * <p>用于统一错误码与异常信息输出。</p>
 */
public enum BitableErrorCode implements ErrorCode {

    PARAM_REQUIRED(51001, "参数不能为空", LEVEL_USER),
    TOKEN_ACQUIRE_FAILED(51002, "飞书Token获取失败", LEVEL_SYSTEM),
    FEISHU_RESPONSE_EMPTY(51003, "飞书接口响应为空", LEVEL_SYSTEM),
    FEISHU_RESPONSE_PARSE_ERROR(51004, "飞书接口响应解析失败", LEVEL_SYSTEM),
    FEISHU_API_ERROR(51005, "飞书接口返回失败", LEVEL_SYSTEM),
    ENTITY_CONSTRUCT_FAILED(51006, "实体构建失败", LEVEL_USER),
    ENTITY_MAPPING_FAILED(51007, "字段映射失败", LEVEL_USER),
    CLIENT_NOT_REGISTERED(51008, "飞书多维表格客户端未注册", LEVEL_SYSTEM),
    APP_CREDENTIALS_MISSING(51009, "飞书应用凭证未配置", LEVEL_SYSTEM),
    TABLE_CONFIG_MISSING(51010, "多维表格配置不存在", LEVEL_SYSTEM),
    TABLE_INDEX_OUT_OF_RANGE(51011, "多维表格配置索引超出范围", LEVEL_SYSTEM),
    VIEW_ID_MISSING(51012, "多维表格视图ID未配置", LEVEL_SYSTEM),
    RECORD_ID_MISSING(51013, "记录ID未配置", LEVEL_SYSTEM),
    APP_TOKEN_MISSING(51014, "多维表格appToken未配置", LEVEL_SYSTEM),
    TABLE_META_MISSING(51015, "多维表格表信息未配置", LEVEL_SYSTEM);

    private final int code;
    private final String msg;
    private final String level;

    BitableErrorCode(int code, String msg, String level) {
        this.code = code;
        this.msg = msg;
        this.level = level;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getLevel() {
        return level;
    }
}
