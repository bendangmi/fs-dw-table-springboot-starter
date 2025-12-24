package cn.bdmcom.core.domain.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 获取 App Access Token 请求体。
 */
@Data
@Builder
public class QueryTokenReq {

    /**
     * 应用ID。
     */
    @JsonProperty("app_id")
    private String appId;

    /**
     * 应用密钥。
     */
    @JsonProperty("app_secret")
    private String appSecret;
}
