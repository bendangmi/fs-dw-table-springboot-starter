package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 批量更新记录请求体。
 */
@Data
public class BatchUpdateRecordReq {

    /**
     * 记录列表。
     */
    private List<Record> records;

    /**
     * 批量更新记录项。
     */
    @Data
    public static class Record {
        /**
         * 记录 ID。
         */
        @JsonProperty("record_id")
        private String recordId;

        /**
         * 字段值集合（字段名 -> 值）。
         */
        @JsonProperty("fields")
        private Map<String, Object> fields;
    }
}
