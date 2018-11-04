package com.water.component.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 *
 */
//@Component("memcachedClient")
//@ImportResource("classpath:application-memcache.xml") //导入xml配置项
public class Memcached {

	   private static final Logger  log =  LoggerFactory.getLogger(Memcached.class);
	    private boolean isOpen; // Memcached 开关
	    private int expires;    // 默认过期时间
	    private MemcachedClient mc;

	    /*
	    add 仅当存储空间中不存在键相同的数据时才保存
	     */
	    public <T> void addWithNoReply(String key, T value) {
	        this.addWithNoReply(key, expires, value);
	    }

	    /**
	     * 设置键值对
	     * @param key     key
	     * @param expires 单位:秒，0 表示永不过期
	     * @param value   必须是一个可序列化的对象, 可以是容器类型如:List，但容器里面保存的对象必须是可序列化的
	     */
	    public <T> void addWithNoReply(String key, int expires, T value) {
	        if (StringUtils.isEmpty(key)) return;
	        try {
	            if (isOpen && mc != null) {
	            	key = key.trim().replace(" ", "_");
	                mc.addWithNoReply(key, expires, value);
	            }
	        } catch (InterruptedException e) {
				log.error("InterruptedException addWithNoReply('{}, '{}', '{}')", key, value+", "+this.expires);
			} catch (MemcachedException e) {
				log.error("MemcachedException addWithNoReply('{}, '{}', '{}')", key, value+", "+this.expires);
			} catch (Exception e){
				log.error("Exception key={}, e={}", key, e);
			}

	    }

	    /*
	    set 无论何时都保存
	     */
	    public <T> void set(String key, T value) {
	        this.set(key, expires, value);
	    }

	    /**
	     *
	     *  删除key
	     * @param key
	     */
	    public void delete(String key){
	    	 if (StringUtils.isEmpty(key)) return;
		        try {
		            if (isOpen && mc != null) {
		            	   key = key.trim().replace(" ", "_");
		                   mc.delete(key);
		            }
		        } catch (TimeoutException e) {
					log.error("TimeoutException delete('{}')", key+", "+this.expires);
				} catch (InterruptedException e) {
					log.error("InterruptedException delete('{})", key+", "+this.expires);
				} catch (MemcachedException e) {
					log.error("MemcachedException delete('{})", key+", "+this.expires);
				} catch (Exception e){
					log.error("Exception key={}, e={}", key, e);
				}
	    }

	    /**
	     * 设置键值对
	     * @param key     key
	     * @param expires 单位:秒，0 表示永不过期
	     * @param value   必须是一个可序列化的对象, 可以是容器类型如:List，但容器里面保存的对象必须是可序列化的
	     */
	    public <T> void set(String key, int expires, T value) {
	        if (StringUtils.isEmpty(key)) return;
	        try {
	            if (isOpen && mc != null) {
	            	key = key.trim().replace(" ", "_");
	                mc.set(key, expires, value);
	            }
	        } catch (TimeoutException e) {
				log.error("TimeoutException set('{}', '{}')", key, value+", "+this.expires);
			} catch (InterruptedException e) {
				log.error("InterruptedException set('{}, '{}')", key, value+", "+this.expires);
			} catch (MemcachedException e) {
				log.error("MemcachedException set('{}, '{}')", key, value+", "+this.expires);
			} catch (Exception e){
				log.error("Exception key={}, e={}", key, e);
			}
	    }

	    /**
	     * 根据key获得值
	     * @param key key
	     * @return value
	     */
	    @SuppressWarnings("unchecked")
		public <T> T get(String key) {
	        try {
	            if (!StringUtils.isEmpty(key) && isOpen && mc != null) {
	            	key = key.trim().replace(" ", "_");
	                return (T)mc.get(key);
	            }
	        } catch (TimeoutException e) {
				log.error("TimeoutException get('{}')", key+", "+this.expires);
			} catch (InterruptedException e) {
				log.error("InterruptedException get('{})", key+", "+this.expires);
			} catch (MemcachedException e) {
				log.error("MemcachedException get('{})", key+", "+this.expires);
			} catch (Exception e){
				log.error("Exception key={}, e={}", key, e);
			}
	        return null;
	    }

	    /**
	     * 给每个原始key加上前缀后，再查。
	     * @param keys                 原始key
	     * @param memcachedKeyPrefix   前缀
	     * @return
	     */
	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public <T> Map<String, T> getMulti(Collection keys, String memcachedKeyPrefix) {
	        if (keys == null || keys.size()<=0) return null;
	        if ("".equals(memcachedKeyPrefix)) {
	            return this.get(keys);
	        }
	        List<String> strList = new ArrayList<String>();
	        for (Object key : keys) {
	            strList.add(memcachedKeyPrefix + String.valueOf(key));
	        }
	        return this.get(strList);
	    }

	    /**
	     * 重载方法
	     * @param keys
	     * @param <T>
	     * @return
	     */
	    @SuppressWarnings("rawtypes")
		public <T> Map<String, T> getMulti(Collection keys) {
	        return getMulti(keys, "");
	    }

	    /**
	     * 批量获取
	     * @param keys keys
	     * @return valueMap
	     */
	    private <T> Map<String, T> get(Collection<String> keys) {
	        Map<String, T> map = null;
	        try {
	            if (keys != null && isOpen && mc != null) {
	                map = mc.get(keys);
	            }
	        } catch (TimeoutException e) {
				log.error("TimeoutException get('{}')", keys+", "+this.expires);
			} catch (InterruptedException e) {
				log.error("InterruptedException get('{})", keys+", "+this.expires);
			} catch (MemcachedException e) {
				log.error("MemcachedException get('{})", keys+", "+this.expires);
			} catch (Exception e){
				log.error("Exception keys={}, e={}", keys, e);
			}
	        return map;
	    }


	    public void setIsOpen(boolean open) {
	        isOpen = open;
	    }

	    public void setExpires(int expires) {
	        this.expires = expires;
	    }

	    public void setMemcachedClient(MemcachedClient mc) {
	        this.mc = mc;
	    }

}
