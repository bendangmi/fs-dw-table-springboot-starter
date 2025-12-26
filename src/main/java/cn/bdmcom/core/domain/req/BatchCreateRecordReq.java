package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 批量新增记录请求体。
 */
@Data
public class BatchCreateRecordReq {

    /**
     * 记录列表。
     */
    private List<Record> records;

    /**
     * 批量新增记录项。
     */
    @Data
    public static class Record {
        /**
         * 字段值集合（字段名 -> 值）。
         */
        @JsonProperty("fields")
        private Map<String, Object> fields;
    }
}
