package com.water.service;

import java.util.Map;

import com.water.modules.model.ZxSysConfig;

public interface SysConfigService {

    /**
     * 根据查询条件查询系统配置
     * @param config
     * @return
     */
    public ZxSysConfig getSysConfig(Map<String, Object> map);
    
    /**
     * 修改系统配置
     * @param config
     * @return
     */
    public boolean updateZxSysConfigByMap(Map<String, Object> map);
}
