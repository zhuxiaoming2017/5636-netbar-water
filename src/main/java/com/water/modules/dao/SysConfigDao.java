package com.water.modules.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.water.modules.model.ZxSysConfig;

/**
 * 
 */
@Component
public interface SysConfigDao {

    /**
     * 根据查询条件查询系统配置
     * @param config
     * @return
     */
    public ZxSysConfig selectZxSysConfigByCond(Map<String, Object> map);

    
    /**
     * 修改系统配置
     * @param config
     * @return
     */
	public int updateZxSysConfigByMap(Map<String, Object> map);
}
