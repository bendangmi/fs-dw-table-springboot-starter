package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 批量删除记录请求体。
 */
@Data
public class BatchDeleteRecordReq {

    /**
     * 记录 ID 列表。
     */
    @JsonProperty("records")
    private List<String> records;
}
