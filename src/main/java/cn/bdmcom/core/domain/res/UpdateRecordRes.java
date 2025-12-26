package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 更新记录响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRecordRes extends AbstractRes<UpdateRecordRes.RecordRes> {

    /**
     * 更新记录返回数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecordRes {

        /**
         * 记录
         */
        private Record record;

        /**
         * 记录明细。
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Record {
            /**
             * 字段
             */
            private Map<String, Object> fields;

            /**
             * 表格ID
             */
            private String id;

            /**
             * 表格ID
             */
            @JsonProperty("record_id")
            private String recordId;
        }
    }
}
