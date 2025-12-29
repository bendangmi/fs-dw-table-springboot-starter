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
 * </pre>
 *
 * <p>appToken 请通过 @FsDwAppBase 注解配置。</p>
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

}
