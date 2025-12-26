package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 列出数据表响应。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListTableRes extends AbstractRes<ListTableRes.TableListData> {

    /**
     * 数据表列表返回数据。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TableListData {
        @JsonProperty("has_more")
        private Boolean hasMore;

        @JsonProperty("page_token")
        private String pageToken;

        private Integer total;

        private List<TableItem> items;
    }

    /**
     * 数据表简要信息。
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TableItem {
        @JsonProperty("table_id")
        private String tableId;

        private Integer revision;

        private String name;
    }
}
