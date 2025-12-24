package cn.bdmcom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 飞书多维表格配置。
 *
 * <p>配置示例：</p>
 * <pre>
 * duoweitable:
 *   app-id: xxx
 *   app-secret: yyy
 *   app-token: app_token
 * </pre>
 */
@ConfigurationProperties(prefix = "duoweitable")
@Data
public class FsDwProperties {

    /**
     * 默认应用 ID。
     */
    private String appId;

    /**
     * 默认应用密钥。
     */
    private String appSecret;

    /**
     * 多维表格 appToken（相当于数据库）。
     */
    private String appToken;
}
