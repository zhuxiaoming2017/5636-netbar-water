package com.water.component.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.water.service.SysConfigService;

@Component
public class SysConfigBean implements CommandLineRunner {

	protected static final Logger log = LoggerFactory.getLogger(SysConfigBean.class);

	private Map<String, Object> configMapCache = new HashMap<String, Object>();
	@Autowired
	protected Properties properties;
	@Autowired
	private SysConfigService sysConfigService;

	public Map<String, Object> getConfigMapCache() {
		return configMapCache;
	}

	public void setConfigMapCache(Map<String, Object> configMapCache) {
		this.configMapCache = configMapCache;
	}

	public Object findValueByKey(String key) {
		return configMapCache.get(key);
	}
	@Override
    public void run(String... args) throws Exception {
		System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
		refreshSysConfig();
		new ScheduledThreadPoolExecutor(1).scheduleWithFixedDelay(new Runnable(){
			public void run() {
				try{
					refreshSysConfig();
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}, 1, 60, TimeUnit.SECONDS);
    }

	public void refreshSysConfig() {
		configMapCache = new HashMap<String,Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("h5statsCode", "cloudmonth");
		configMapCache.put("cloudmonth", sysConfigService.getSysConfig(queryMap));
	}
	

}
