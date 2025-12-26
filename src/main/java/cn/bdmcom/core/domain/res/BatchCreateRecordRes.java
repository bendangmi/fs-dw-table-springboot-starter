package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 批量新增记录响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchCreateRecordRes extends AbstractRes<BatchCreateRecordRes.BatchRecordData> {

    /**
     * 批量新增记录返回数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatchRecordData {
        /**
         * 记录列表。
         */
        private List<RecordItem> records;
    }
}
