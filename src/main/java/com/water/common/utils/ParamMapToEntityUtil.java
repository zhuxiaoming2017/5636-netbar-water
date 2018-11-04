package com.water.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class ParamMapToEntityUtil {
	@SuppressWarnings("unchecked")
	public static <T> T format(Map<String,String[]> requestMap,Class<T> clazz){
		Map<String,String> map = new HashMap<String, String>();
		String[] values = null;
		for(Map.Entry<String, String[]> entry : requestMap.entrySet()){
    		values = entry.getValue();
    		if(values.length != 0 && !"".equals(values[0].trim())){
    			map.put(entry.getKey(), values[0]);
    		}
    		
    	}
		if(clazz == Map.class){
			return (T) map;
		}
		return JSON.parseObject(JSON.toJSONString(map), clazz);
	}
	/**
	 * 格式化保存对象(需要保留空字符)
	 * @param requestMap
	 * @param clazz
	 * @return
	 */
	public static <T> T formatSaveEntity(Map<String,String[]> requestMap,Class<T> clazz){
		Map<String,String> map = new HashMap<String, String>();
		String[] values = null;
		for(Map.Entry<String, String[]> entry : requestMap.entrySet()){
			String value = "";
    		values = entry.getValue();
    		if(values.length != 0){
    			for(int i = 0; i < values.length; i++){
    				if(i == values.length - 1)
    					value += values[0].trim();
    				else
    					value += values[0].trim()+",";
        			
        		}
    			map.put(entry.getKey(), value);
    		}
    		
    	}
		if(clazz == Map.class){
			return (T) map;
		}
		return JSON.parseObject(JSON.toJSONString(map), clazz);
	}
}
