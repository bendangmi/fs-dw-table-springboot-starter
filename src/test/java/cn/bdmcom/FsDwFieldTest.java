package cn.bdmcom;

import cn.bdmcom.core.domain.TypeEnum;
import cn.bdmcom.core.domain.req.AddFieldReq;
import cn.bdmcom.core.domain.req.UpdateFieldReq;
import cn.bdmcom.core.domain.res.AddFieldRes;
import cn.bdmcom.core.domain.res.DeleteFieldRes;
import cn.bdmcom.core.domain.res.TableFieldListRes;
import cn.bdmcom.core.domain.res.UpdateFieldRes;
import cn.bdmcom.core.helper.FsDwFieldHelper;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author: corbin
 * @createTime: 2025/12/24 09:35
 * @description: 飞书多维表格字段测试类
 */
@Slf4j
@SpringBootTest
class FsDwFieldTest {

    /**
     * 列出所有字段
     */
    @Test
    void testFieldQuery() {
        List<TableFieldListRes.TableField> tableFields = FsDwFieldHelper.listFields(TestTable.class);
        log.info("tableFields:{}", tableFields);
    }

    /**
     * 新增字段
     */
    @Test
    void testFieldAdd() {
        AddFieldReq addFieldReq = new AddFieldReq("测试字段", TypeEnum.TEXT);
        AddFieldRes field = FsDwFieldHelper.createField(TestTable.class, addFieldReq);
        log.info("field:{}", field);
    }

    /**
     * 更新字段
     */
    @Test
    void testFieldUpdate() {
        UpdateFieldReq updateFieldReq = new UpdateFieldReq( "测试字段1", TypeEnum.TEXT);
        UpdateFieldRes update = FsDwFieldHelper.updateFieldByName(TestTable.class, "测试字段", updateFieldReq);
        log.info("update:{}", update);
    }

    /**
     * 删除字段
     */
    @Test
    void testFieldDelete() {
        DeleteFieldRes fieldRes = FsDwFieldHelper.deleteFieldByName(TestTable.class, "测试字段1");
        log.info("fieldRes:{}", fieldRes);
    }
}
