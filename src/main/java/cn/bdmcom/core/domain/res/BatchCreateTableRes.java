package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 批量新增数据表响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchCreateTableRes extends AbstractRes<BatchCreateTableRes.BatchCreateData> {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatchCreateData {
        /**
         * 数据表 ID 列表。
         */
        @JsonProperty("table_ids")
        private List<String> tableIds;
    }
}
