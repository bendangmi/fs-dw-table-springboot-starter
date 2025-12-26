package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 查询记录请求体。
 *
 * <p>可按视图、字段、排序、过滤条件组合查询。</p>
 */
@Data
public class QueryRecordReq {

    /**
     * 视图ID。
     */
    @JsonProperty("view_id")
    private String viewId;

    /**
     * 字段名称列表（field_names），为空时默认返回视图字段。
     */
    @JsonProperty("field_names")
    private List<String> fieldNames;

    /**
     * 排序条件。
     */
    private List<Sort> sort;

    /**
     * 是否包含自动字段（系统字段）。
     */
    @JsonProperty("automatic_fields")
    private Boolean automaticFields;

    /**
     * 过滤条件。
     */
    private Filter filter;

    /**
     * 分页 token。
     */
    @JsonProperty("page_token")
    private String pageToken;

    /**
     * 当前页（从 1 开始，仅用于内部分页控制）。
     */
    @JsonIgnore
    private Integer pageNo;

    /**
     * 每页数量。
     */
    @JsonProperty("page_size")
    private Integer pageSize;


    /**
     * 排序条件。
     */
    @Data
    public static class Sort {
        /**
         * 字段名称。
         */
        @JsonProperty("field_name")
        private String fieldName;
        /**
         * 排序方式（true=倒序）。
         */
        private Boolean desc;
    }

    /**
     * 过滤条件树。
     */
    @Data
    public static class Filter {
        /**
         * 逻辑关系（and/or）。
         */
        private String conjunction;

        /**
         * 条件列表。
         */
        private List<Condition> conditions;

        /**
         * 子条件分组（仅支持一层嵌套）。
         */
        private List<Filter> children;


        /**
         * 过滤条件项。
         */
        @Data
        public static class Condition {
            /**
             * 字段名称。
             */
            @JsonProperty("field_name")
            private String fieldName;
            /**
             * 操作符（如 is、contains 等）。
             */
            private String operator;
            /**
             * 值。
             */
            private Object value;
        }
    }
}
