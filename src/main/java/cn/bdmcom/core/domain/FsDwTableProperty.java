package cn.bdmcom.core.domain;

import java.lang.annotation.*;

/**
 * 飞书多维表格字段映射注解。
 *
 * <p>用于标记实体字段与多维表格列名的对应关系。</p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FsDwTableProperty {

    /**
     * 表格列名（优先级低于 field）。
     *
     * @return 列名字符串
     */
    String[] value() default {};

    /**
     * 字段描述（文档用途，不参与映射逻辑）。
     *
     * @return 字段描述字符串
     */
    String desc() default "";

    /**
     * 字段名（与多维表格列名一致，优先级最高）。
     *
     * @return 字段名字符串
     */
    String field() default "";

    /**
     * 字段排序顺序。
     *
     * @return 排序值，数值越小越靠前
     */
    int order() default Integer.MAX_VALUE;

    /**
     * 字段类型（保留字段，可用于后续类型处理）。
     *
     * @return 字段类型枚举
     */
    TypeEnum type() default TypeEnum.TEXT;

    /**
     * 枚举选项（如果type类型为多选或者单选，选项从options中获取）
     *
     * @return 枚举选项列表
     */
    String[] options() default {};
}
