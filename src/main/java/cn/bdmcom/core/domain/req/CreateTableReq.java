package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 新增数据表请求体。
 */
@Data
public class CreateTableReq {

    /**
     * 数据表信息。
     */
    private Table table;

    /**
     * 数据表定义。
     */
    @Data
    public static class Table {
        /**
         * 数据表名称。
         */
        private String name;

        /**
         * 默认视图名称。
         */
        @JsonProperty("default_view_name")
        private String defaultViewName;

        /**
         * 初始字段定义。
         */
        private List<Field> fields;
    }

    /**
     * 字段定义。
     */
    @Data
    public static class Field {
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
         * UI 类型。
         */
        @JsonProperty("ui_type")
        private String uiType;

        /**
         * 字段属性（结构较复杂，使用 Map 承载）。
         */
        private Map<String, Object> property;
    }
}
