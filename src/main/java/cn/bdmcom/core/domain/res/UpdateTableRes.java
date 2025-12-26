package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 更新数据表响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTableRes extends AbstractRes<UpdateTableRes.UpdateTableData> {

    /**
     * 更新数据表返回数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateTableData {
        /**
         * 数据表名称。
         */
        private String name;
    }
}
