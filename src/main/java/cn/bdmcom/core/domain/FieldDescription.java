package cn.bdmcom.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 多维表格字段描述信息。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldDescription {

    /**
     * 是否禁止同步描述到表单。
     */
    @JsonProperty("disable_sync")
    private Boolean disableSync;

    /**
     * 描述内容。
     */
    private String text;
}
