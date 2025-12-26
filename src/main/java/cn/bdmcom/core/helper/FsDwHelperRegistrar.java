package cn.bdmcom.core.helper;

import cn.bdmcom.config.FsDwProperties;
import cn.bdmcom.core.service.FsDwFieldService;
import cn.bdmcom.core.service.FsDwRecordService;
import cn.bdmcom.core.service.FsDwTableService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FsDwHelper 自动注册器。
 *
 * <p>Spring 环境下自动注册服务与配置。</p>
 */
public class FsDwHelperRegistrar {

    @Autowired
    private FsDwRecordService fsDwRecordService;

    @Autowired
    private FsDwFieldService fsDwFieldService;

    @Autowired
    private FsDwProperties properties;

    @Autowired
    private FsDwTableService fsDwTableService;

    /**
     * 注册辅助类所需的服务与配置。
     */
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
