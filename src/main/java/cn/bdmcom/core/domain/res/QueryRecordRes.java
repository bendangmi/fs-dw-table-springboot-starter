package cn.bdmcom.core.domain.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 查询记录响应体。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryRecordRes extends AbstractRes<QueryRecordRes.RecordRes> {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecordRes {

        /**
         * 是否存在更多
         */
        @JsonProperty("has_more")
        private Boolean hasMore;

        /**
         * 记录
         */
        private Integer total;

        /**
         * 下一页 token
         */
        @JsonProperty("page_token")
        private String pageToken;

        /**
         * 记录列表
         */
        private List<Item> items;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            /**
             * 记录ID
             */
            @JsonProperty("record_id")
            private String recordId;
            /**
             * 最后修改时间
             */
            @JsonProperty("last_modified_time")
            private String lastModifiedTime;
            /**
             * 创建时间
             */
            @JsonProperty("created_time")
            private String createdTime;
            /**
             * 字段
             */
            private Map<String, Object> fields;
        }
    }
}
