package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 多维表格记录的用户信息。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordUser {

    /**
     * 用户 ID。
     */
    private String id;

    /**
     * 中文名称。
     */
    private String name;

    /**
     * 英文名称。
     */
    @JsonProperty("en_name")
    private String enName;

    /**
     * 邮箱。
     */
    private String email;

    /**
     * 头像链接。
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;
}
