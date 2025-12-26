package cn.bdmcom.core.domain.req;

import lombok.Data;

import java.util.List;

/**
 * 批量新增数据表请求体。
 */
@Data
public class BatchCreateTableReq {

    /**
     * 数据表名称列表。
     */
    private List<Table> tables;

    /**
     * 批量创建数据表项。
     */
    @Data
    public static class Table {
        /**
         * 数据表名称。
         */
        private String name;
    }
}
