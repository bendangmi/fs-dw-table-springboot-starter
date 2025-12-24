package cn.bdmcom.core.helper;

import cn.bdmcom.config.FsDwProperties;
import cn.bdmcom.core.service.FsDwFieldService;
import cn.bdmcom.core.service.FsDwRecordService;
import cn.bdmcom.core.service.FsDwTableService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * FsDwHelper 自动注册器。
 *
 * <p>Spring 环境下自动注册服务与配置。</p>
 */
@Component
public class FsDwHelperRegistrar {

    @Resource
    private FsDwRecordService fsDwRecordService;

    @Resource
    private FsDwFieldService fsDwFieldService;

    @Resource
    private FsDwProperties properties;

    @Resource
    private FsDwTableService fsDwTableService;

    @PostConstruct
    public void register() {
        FsDwRecordHelper.registerServices(fsDwRecordService);
        FsDwRecordHelper.registerProperties(properties);
        FsDwTableHelper.registerServices(fsDwTableService);
        FsDwTableHelper.registerProperties(properties);
        FsDwFieldHelper.registerServices(fsDwFieldService);
        FsDwFieldHelper.registerProperties(properties);
    }
}
