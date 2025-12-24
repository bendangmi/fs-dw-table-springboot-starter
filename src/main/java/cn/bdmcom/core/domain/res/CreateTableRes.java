package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 新增数据表响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTableRes extends AbstractRes<CreateTableRes.CreateTableData> {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateTableData {
        /**
         * 数据表 ID。
         */
        @JsonProperty("table_id")
        private String tableId;

        /**
         * 默认视图 ID。
         */
        @JsonProperty("default_view_id")
        private String defaultViewId;

        /**
         * 初始字段 ID 列表。
         */
        @JsonProperty("field_id_list")
        private List<String> fieldIdList;
    }
}
