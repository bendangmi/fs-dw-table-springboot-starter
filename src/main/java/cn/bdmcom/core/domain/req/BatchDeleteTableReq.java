package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 批量删除数据表请求体。
 */
@Data
public class BatchDeleteTableReq {

    /**
     * 待删除的数据表 ID 列表。
     */
    @JsonProperty("table_ids")
    private List<String> tableIds;
}
