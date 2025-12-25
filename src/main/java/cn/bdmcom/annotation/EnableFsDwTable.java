package cn.bdmcom.annotation;

import cn.bdmcom.autoconfigure.FsDwAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable Feishu Bitable starter components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FsDwAutoConfiguration.class)
public @interface EnableFsDwTable {
}
