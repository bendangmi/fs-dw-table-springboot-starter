package cn.bdmcom;

import cn.bdmcom.core.domain.DwLambdaQueryWrapper;
import cn.bdmcom.core.domain.res.AddRecordRes;
import cn.bdmcom.core.domain.res.BatchCreateRecordRes;
import cn.bdmcom.core.domain.res.DeleteRecordRes;
import cn.bdmcom.core.domain.res.UpdateRecordRes;
import cn.bdmcom.core.helper.FsDwRecordHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: corbin
 * @createTime: 2025/12/22 16:35
 * @description: PLM项目飞书多维表格测试
 */
@Slf4j
@SpringBootTest
class FsDwRecordTest {


    /**
     * 查询所有记录
     */
    @Test
    void testRecordQuery() {
        List<TestTable> list = FsDwRecordHelper.queryRecords(TestTable.class);
        log.info("list:{}", list);

    }

    /**
     * 新增单条记录
     */
    @Test
    void testRecordAdd() {
        TestTable add = new TestTable();
        add.setId(1);
        add.setName("测试1");
        add.setAge(18);
        add.setSex("男");
        add.setAddress("上海");
        add.setPhone("12345678901");
        add.setEmail("<EMAIL>");
        add.setRemark("测试");
        add.setCreateTime(LocalDateTime.now());
        add.setUpdateTime(LocalDateTime.now());
        AddRecordRes addRecordRes = FsDwRecordHelper.addRecord(add);
        log.info("addRecordRes:{}", addRecordRes.getData().getRecord().getRecordId());
    }

    /**
     * 新增多条记录
     */
    @Test
    void testRecordBatchAdd() {
        List<TestTable> addList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            TestTable add = new TestTable();
            add.setId(i);
            add.setName("测试" + i);
            add.setAge(18);
            add.setSex("男");
            add.setAddress("上海");
            add.setPhone("12345678901");
            add.setEmail("<EMAIL>");
            add.setRemark("测试");
            add.setCreateTime(LocalDateTime.now());
            add.setUpdateTime(LocalDateTime.now());
            addList.add(add);
        }
        BatchCreateRecordRes batchCreateRecordRes = FsDwRecordHelper.batchCreateRecords(TestTable.class, addList);
        log.info("batchCreateRecordRes:{}", batchCreateRecordRes);
    }

    /**
     * 条件查询
     */
    @Test
    void testRecordQueryCondition() {

        // 按照名称查询
        DwLambdaQueryWrapper<TestTable> wrapper = new DwLambdaQueryWrapper<>();
        wrapper.eq(TestTable::getName, "测试1");
        List<TestTable> list = FsDwRecordHelper.queryRecords(TestTable.class, wrapper);
        log.info("list:{}", list);


        // 范围查询
        wrapper = new DwLambdaQueryWrapper<>();
        wrapper.between(TestTable::getId, 100, 200);
        list = FsDwRecordHelper.queryRecords(TestTable.class, wrapper);
        log.info("list:{}", list);
    }

    /**
     * 分页查询
     */
    @Test
    void testRecordQueryPage() {
        DwLambdaQueryWrapper<TestTable> wrapper = new DwLambdaQueryWrapper<>();
        wrapper.pageSize(20);
        wrapper.pageNo(2);
        List<TestTable> list = FsDwRecordHelper.queryRecords(TestTable.class, wrapper);
        log.info("list:{}", list);
    }

    /**
     * 修改记录
     */
    @Test
    void testRecordUpdate() {
        TestTable update = new TestTable();
        update.setRecordId("recv6bFu8EfXFQ");
        update.setName("测试1");
        update.setAge(18);
        update.setSex("男");
        update.setAddress("上海");
        update.setPhone("12345678901");
        update.setEmail("<EMAIL>");
        update.setRemark("测试");
        update.setCreateTime(LocalDateTime.now());
        update.setUpdateTime(LocalDateTime.now());
        UpdateRecordRes updateRecordRes = FsDwRecordHelper.updateRecord(update);
        log.info("updateRecordRes:{}", updateRecordRes.getData().getRecord().getRecordId());
    }

    /**
     * 删除记录
     */
    @Test
    void testRecordDelete() {
        TestTable delete = new TestTable();
        delete.setRecordId("recv6bFu8EfXFQ");
        DeleteRecordRes deleteRes = FsDwRecordHelper.deleteRecord(delete);
        log.info("deleteRecordRes:{}", deleteRes.getData().getDeleted());
    }

}
