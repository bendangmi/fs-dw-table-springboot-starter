package cn.bdmcom.annotation;

import cn.bdmcom.autoconfigure.FsDwAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable Feishu Bitable starter components.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FsDwAutoConfiguration.class)
public @interface EnableFsDwTable {
}
