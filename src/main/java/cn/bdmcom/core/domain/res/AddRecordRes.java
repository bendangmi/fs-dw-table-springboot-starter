package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 新增记录响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddRecordRes extends AbstractRes<AddRecordRes.RecordRes> {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecordRes {

        /**
         * 记录
         */
        private Record record;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Record {
           /**
            * 表格ID
            */
           private String id;
           /**
            * 记录ID
            */
           @JsonProperty("record_id")
           private String recordId;
           /**
            * 字段
            */
           private Map<String, Object> fields;
       }
    }
}
