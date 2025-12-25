package cn.bdmcom.autoconfigure;

import cn.bdmcom.config.FsDwProperties;
import cn.bdmcom.core.helper.FsDwHelperRegistrar;
import cn.bdmcom.core.service.FsDwFieldService;
import cn.bdmcom.core.service.FsDwRecordService;
import cn.bdmcom.core.service.FsDwTableService;
import cn.bdmcom.core.service.FsDwTokenService;
import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 飞书多维表格 Starter 自动配置。
 */
@AutoConfiguration
@EnableConfigurationProperties(FsDwProperties.class)
@ForestScan(basePackages = "cn.bdmcom.core.api")
public class FsDwAutoConfiguration {

    @Bean
    public FsDwTokenService bdmFsDwTokenService() {
        return new FsDwTokenService();
    }

    @Bean
    public FsDwRecordService bdmFsDwRecordService() {
        return new FsDwRecordService();
    }

    @Bean
    public FsDwFieldService bdmFsDwFieldService() {
        return new FsDwFieldService();
    }

    @Bean
    public FsDwTableService bdmFsDwTableService() {
        return new FsDwTableService();
    }

    @Bean
    public FsDwHelperRegistrar bdmFsDwHelperRegistrar() {
        return new FsDwHelperRegistrar();
    }
}
