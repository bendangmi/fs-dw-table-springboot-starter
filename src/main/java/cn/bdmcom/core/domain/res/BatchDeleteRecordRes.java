package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 批量删除记录响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchDeleteRecordRes extends AbstractRes<BatchDeleteRecordRes.BatchDeleteData> {

    /**
     * 批量删除记录返回数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatchDeleteData {
        /**
         * 删除结果列表。
         */
        private List<DeleteRecord> records;
    }

    /**
     * 删除记录结果。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeleteRecord {
        /**
         * 是否删除成功。
         */
        private Boolean deleted;

        /**
         * 记录 ID。
         */
        @JsonProperty("record_id")
        private String recordId;
    }
}
