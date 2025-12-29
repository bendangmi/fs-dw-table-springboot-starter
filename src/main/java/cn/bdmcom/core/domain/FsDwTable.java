package cn.bdmcom.core.domain;

import java.lang.annotation.*;

/**
 * 飞书多维表格表信息注解。
 *
 * <p>appToken 由 @FsDwAppBase 提供，tableId/viewId 由实体类定义。</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FsDwTable {

    /**
     * 数据表名称。
     *
     * @return 数据表名称
     */
    String name();

    /**
     * 数据表 ID。
     *
     * @return 数据表 ID
     */
    String tableId();

    /**
     * 默认视图名称（用于创建数据表时设置默认视图）。
     *
     * @return 默认视图名称
     */
    String defaultViewName() default "";

    /**
     * 视图 ID。
     *
     * @return 视图 ID
     */
    String viewId();
}
