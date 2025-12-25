package cn.bdmcom.core.domain.req;

import cn.bdmcom.core.domain.FieldDescription;
import cn.bdmcom.core.domain.TypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * 更新字段请求体。
 */
@Data
public class UpdateFieldReq {

    /**
     * 字段名称。
     */
    @JsonProperty("field_name")
    private String fieldName;

    /**
     * 字段类型。
     */
    private Integer type;

    /**
     * 字段属性（结构较复杂，使用 Map 承载）。
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

    public UpdateFieldReq(String fieldName, TypeEnum typeEnum) {
        this.fieldName = fieldName;
        this.type = typeEnum.getType();
    }
}
