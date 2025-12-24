package cn.bdmcom.core.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型枚举。
 *
 * <p>定义飞书多维表格常用字段类型，用于 {@link FsDwTableProperty} 注解标注。</p>
 */
@Getter
@AllArgsConstructor
public enum TypeEnum {

    /**
     * 文本类型
     */
    TEXT(1, "TEXT", "文本"),

    /**
     * 数字类型
     */
    NUMBER(2, "NUMBER", "数字"),

    /**
     * 单选类型
     */
    SINGLE_SELECT(3, "SINGLE_SELECT", "单选"),

    /**
     * 多选类型
     */
    MULTI_SELECT(4, "MULTI_SELECT", "多选"),

    /**
     * 日期类型
     */
    DATE(5, "DATE", "日期"),

    /**
     * 复选框
     */
    CHECKBOX(7, "CHECKBOX", "复选框"),

    /**
     * 文本链接类型
     */
    TEXT_URL(15, "TEXT_URL", "文本链接"),

    /**
     * 文本文件类型
     */
    TEXT_FILE(17, "TEXT_FILE", "文本文件"),

    ;


    private final Integer type;
    private final String code;
    private final String desc;

    /**
     * 根据编码获取枚举值
     *
     * @param code 类型编码
     * @return 对应的枚举值，未找到返回null
     */
    public static TypeEnum getByCode(String code) {
        for (TypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
