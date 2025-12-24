package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 批量获取记录响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchGetRecordRes extends AbstractRes<BatchGetRecordRes.BatchGetData> {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatchGetData {
        /**
         * 记录列表。
         */
        private List<RecordItem> records;

        /**
         * 无权限访问的记录 ID 列表。
         */
        @JsonProperty("forbidden_record_ids")
        private List<String> forbiddenRecordIds;

        /**
         * 不存在的记录 ID 列表。
         */
        @JsonProperty("absent_record_ids")
        private List<String> absentRecordIds;
    }
}
