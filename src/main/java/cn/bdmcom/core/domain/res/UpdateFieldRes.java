package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 更新字段响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateFieldRes extends AbstractRes<UpdateFieldRes.FieldData> {

    /**
     * 更新字段返回数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FieldData {
        /**
         * 字段信息。
         */
        private FieldInfo field;
    }
}
