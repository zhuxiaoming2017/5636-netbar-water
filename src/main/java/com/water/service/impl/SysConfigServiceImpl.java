package com.water.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.water.modules.dao.SysConfigDao;
import com.water.modules.model.ZxSysConfig;
import com.water.service.SysConfigService;

/**
 * 系统配置
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigDao sysConfigDao;

    /**
     * 根据查询条件查询系统配置
     * @param config
     * @return
     */
    public ZxSysConfig getSysConfig(Map<String, Object> map) {
        return sysConfigDao.selectZxSysConfigByCond(map);
    }
    
    /**
     * 修改系统配置
     * @param config
     * @return
     */
    public boolean updateZxSysConfigByMap(Map<String, Object> map){
    	int num=  sysConfigDao.updateZxSysConfigByMap(map);
    	if(num > 0){
    		return true;
    	}
    	return false;
    }
}
