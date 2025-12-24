package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 获取 App Access Token 响应体。
 */
@Data
public class QueryTokenRes {

    /**
     * 应用访问令牌。
     */
    @JsonProperty("app_access_token")
    private String appAccessToken;

    /**
     * 状态码。
     */
    private Integer code;

    /**
     * 过期时间（秒）。
     */
    private Integer expire;

    /**
     * 错误信息。
     */
    private String msg;

    /**
     * 租户访问令牌（如有）。
     */
    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;
}
