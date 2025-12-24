package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 删除记录响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteRecordRes extends AbstractRes<DeleteRecordRes.RecordRes> {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecordRes {
        /**
         * 是否删除成功
         */
        private Boolean deleted;
        /**
         * 记录ID
         */
        @JsonProperty("record_id")
        private String recordId;
    }
}
