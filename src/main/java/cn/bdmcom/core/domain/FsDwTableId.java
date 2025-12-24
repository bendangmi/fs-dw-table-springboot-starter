package cn.bdmcom.core.domain;

import java.lang.annotation.*;

/**
 * 飞书多维表格记录 ID 注解。
 *
 * <p>用于将 record_id 回显到实体字段，不参与写入。</p>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FsDwTableId {
}
