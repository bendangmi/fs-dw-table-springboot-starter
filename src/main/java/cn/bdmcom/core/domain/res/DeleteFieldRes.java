package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 删除字段响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteFieldRes extends AbstractRes<DeleteFieldRes.DeleteFieldData> {

    /**
     * 删除字段返回数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeleteFieldData {
        /**
         * 字段 ID。
         */
        @JsonProperty("field_id")
        private String fieldId;

        /**
         * 是否删除成功。
         */
        private Boolean deleted;
    }
}
