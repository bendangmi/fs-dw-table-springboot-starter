package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * 新增记录请求体。
 */
@Data
public class AddRecordReq {

    /**
     * 字段值集合（字段名 -> 值）。
     */
    @JsonProperty("fields")
    private Map<String, Object> fields;
}
