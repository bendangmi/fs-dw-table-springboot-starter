package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 多维表格字段列表响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableFieldListRes extends AbstractRes<TableFieldListRes.FieldListData> {

    /**
     * 字段列表数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FieldListData {
        /**
         * 字段列表。
         */
        private List<TableField> items;

        /**
         * 是否还有下一页。
         */
        @JsonProperty("has_more")
        private Boolean hasMore;

        /**
         * 下一页 token。
         */
        @JsonProperty("page_token")
        private String pageToken;
    }

    /**
     * 字段信息。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TableField {

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
    }
}
