package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 飞书接口通用响应结构。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractRes<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 数据
     */
    private T data;

    /**
     * 错误信息
     */
    private String msg;
    /**
     * 错误码
     */
    private String error;
}
