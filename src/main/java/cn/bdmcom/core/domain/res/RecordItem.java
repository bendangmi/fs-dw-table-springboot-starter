package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * 多维表格记录详情。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordItem {

    /**
     * 记录字段。
     */
    private Map<String, Object> fields;

    /**
     * 记录 ID。
     */
    @JsonProperty("record_id")
    private String recordId;

    /**
     * 兼容字段（部分接口返回 id）。
     */
    private String id;

    /**
     * 创建人信息。
     */
    @JsonProperty("created_by")
    private RecordUser createdBy;

    /**
     * 修改人信息。
     */
    @JsonProperty("last_modified_by")
    private RecordUser lastModifiedBy;

    /**
     * 创建时间（毫秒级时间戳）。
     */
    @JsonProperty("created_time")
    private Long createdTime;

    /**
     * 最近修改时间（毫秒级时间戳）。
     */
    @JsonProperty("last_modified_time")
    private Long lastModifiedTime;

    /**
     * 分享链接。
     */
    @JsonProperty("shared_url")
    private String sharedUrl;

    /**
     * 记录链接。
     */
    @JsonProperty("record_url")
    private String recordUrl;
}
