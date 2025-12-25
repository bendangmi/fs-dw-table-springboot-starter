package cn.bdmcom.core.domain.res;

import cn.bdmcom.core.domain.FieldDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * 多维表格字段详情。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldInfo {

    /**
     * 字段 ID。
     */
    @JsonProperty("field_id")
    private String fieldId;

    /**
     * 字段名称。
     */
    @JsonProperty("field_name")
    private String fieldName;

    /**
     * 是否为主键字段。
     */
    @JsonProperty("is_primary")
    private Boolean primary;

    /**
     * 字段类型编码。
     */
    private Integer type;

    /**
     * 字段属性扩展信息。
     */
    private Map<String, Object> property;

    /**
     * 字段描述。
     */
    private FieldDescription description;

    /**
     * 字段 UI 类型。
     */
    @JsonProperty("ui_type")
    private String uiType;

    /**
     * 是否为隐藏字段。
     */
    @JsonProperty("is_hidden")
    private Boolean hidden;
}
