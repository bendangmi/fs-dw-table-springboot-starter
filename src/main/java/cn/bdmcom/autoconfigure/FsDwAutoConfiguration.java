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

    /**
     * 创建 Token 服务 Bean。
     *
     * @return Token 服务
     */
    @Bean
    public FsDwTokenService bdmFsDwTokenService() {
        return new FsDwTokenService();
    }

    /**
     * 创建记录服务 Bean。
     *
     * @return 记录服务
     */
    @Bean
    public FsDwRecordService bdmFsDwRecordService() {
        return new FsDwRecordService();
    }

    /**
     * 创建字段服务 Bean。
     *
     * @return 字段服务
     */
    @Bean
    public FsDwFieldService bdmFsDwFieldService() {
        return new FsDwFieldService();
    }

    /**
     * 创建表格服务 Bean。
     *
     * @return 表格服务
     */
    @Bean
    public FsDwTableService bdmFsDwTableService() {
        return new FsDwTableService();
    }

    /**
     * 创建辅助注册器 Bean。
     *
     * @return 辅助注册器
     */
    @Bean
    public FsDwHelperRegistrar bdmFsDwHelperRegistrar() {
        return new FsDwHelperRegistrar();
    }
}
