package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 批量获取记录请求体。
 */
@Data
public class BatchGetRecordReq {

    /**
     * 记录 ID 列表。
     */
    @JsonProperty("record_ids")
    private List<String> recordIds;

    /**
     * 用户 ID 类型。
     */
    @JsonProperty("user_id_type")
    private String userIdType;

    /**
     * 是否返回分享链接。
     */
    @JsonProperty("with_shared_url")
    private Boolean withSharedUrl;

    /**
     * 是否返回自动计算字段。
     */
    @JsonProperty("automatic_fields")
    private Boolean automaticFields;
}
