package cn.bdmcom;

import cn.bdmcom.core.domain.FsDwTable;
import cn.bdmcom.core.domain.FsDwTableId;
import cn.bdmcom.core.domain.FsDwTableProperty;
import cn.bdmcom.core.domain.TypeEnum;
import lombok.Data;


import java.time.LocalDateTime;

/**
 * @author: corbin
 * @createTime: 2025/12/24 10:33
 * @description: 测试表格
 */
@Data
@FsDwTable(name = "测试表格1", tableId = "tblbrm9iT0V3Z671", viewId = "vewRljgMMT")
public class TestTable {

    @FsDwTableId
    private String recordId;

    @FsDwTableProperty(value = "id", order = 0, type = TypeEnum.NUMBER)
    private Integer id;

    @FsDwTableProperty(value = "姓名", order = 1, type = TypeEnum.TEXT)
    private String name;

    @FsDwTableProperty(value = "年龄", order = 2, type = TypeEnum.NUMBER)
    private Integer age;

    @FsDwTableProperty(value = "性别", order = 3, type = TypeEnum.SINGLE_SELECT, options = {"男", "女"})
    private String sex;

    @FsDwTableProperty(value = "地址", order = 4, type = TypeEnum.TEXT)
    private String address;

    @FsDwTableProperty(value = "手机号", order = 5, type = TypeEnum.TEXT)
    private String phone;

    @FsDwTableProperty(value = "邮箱", order = 6, type = TypeEnum.TEXT)
    private String email;

    @FsDwTableProperty(value = "备注", order = 7, type = TypeEnum.TEXT)
    private String remark;

    @FsDwTableProperty(value = "创建时间", order = 8, type = TypeEnum.DATE)
    private LocalDateTime createTime;

    @FsDwTableProperty(value = "更新时间", order = 9, type = TypeEnum.DATE)
    private LocalDateTime updateTime;
}
