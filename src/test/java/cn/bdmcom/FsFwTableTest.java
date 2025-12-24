package cn.bdmcom;

import cn.bdmcom.core.domain.res.CreateTableRes;
import cn.bdmcom.core.domain.res.DeleteTableRes;
import cn.bdmcom.core.domain.res.ListTableRes;
import cn.bdmcom.core.domain.res.UpdateTableRes;
import cn.bdmcom.core.helper.FsDwTableHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author: corbin
 * @createTime: 2025/12/24 09:37
 * @description: 飞书多维表格 表测试
 */
@Slf4j
@SpringBootTest
class FsFwTableTest {

    /**
     * 列出所有表
     */
    @Test
    void testTableQuery() {
        List<ListTableRes.TableItem> tableItems = FsDwTableHelper.listAllTables();
        log.info("tableItems:{}", tableItems);
    }

    /**
     * 新增表（单个）
     */
    @Test
    void testTableAdd() {
        CreateTableRes table = FsDwTableHelper.createTable(TestTable.class);
        log.info("table:{}", table);
    }

    /**
     * 更新表（只能修改表名）
     */
    @Test
    void testTableUpdate() {
        UpdateTableRes updateTableRes = FsDwTableHelper.updateTable(TestTable.class);
        log.info("updateTableRes:{}", updateTableRes.getData().getName());
    }

    /**
     * 删除表
     */
    @Test
    void testTableDelete() {
        DeleteTableRes deleteTableRes = FsDwTableHelper.deleteTable(TestTable.class);
        log.info("deleteTableRes:{}", deleteTableRes.getData());
    }
}
