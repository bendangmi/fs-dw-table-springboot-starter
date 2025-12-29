package cn.bdmcom.core.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 飞书多维表格 App 基础信息注解。
 *
 * <p>用于标注 appToken，建议放在实体父类上并通过继承获取。</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FsDwAppBase {

    /**
     * 多维表格 appToken（相当于一个 Excel 文件）。
     *
     * @return appToken
     */
    String appToken();
}
