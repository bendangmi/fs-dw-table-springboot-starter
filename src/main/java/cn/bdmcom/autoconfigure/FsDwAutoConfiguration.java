package cn.bdmcom.autoconfigure;

import cn.bdmcom.config.FsDwProperties;
import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * 飞书多维表格 Starter 自动配置。
 */
@AutoConfiguration
@EnableConfigurationProperties(FsDwProperties.class)
@ComponentScan(basePackages = "cn.bdmcom.core")
@ForestScan(basePackages = "cn.bdmcom.core.api")
public class FsDwAutoConfiguration {
}
