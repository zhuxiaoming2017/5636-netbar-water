package com.water.component.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.water.common.utils.Constants;
import com.water.component.bean.Memcached;
import com.water.modules.model.FrontLoginUserBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class WebSecurityManager {

    //@Autowired
    //private Memcached memcachedClient;

    //保存和更新登录信息到session、memcached
    public void  saveLogin(FrontLoginUserBean userLoginBean,HttpSession session){
        session.setAttribute(Constants.FRONT_LOGIN_USER_BEAN_SESSION_KEY,userLoginBean);

        //memcachedClient.set(userLoginKey,userLoginBean);
        LocalContext.setFrontLoginUserBean(userLoginBean);
    }

    /**
     * 用户登录信息
     * @return
     */
    public String getUserLoginKey(String uName)
    {
        StringBuilder cacheKey = new StringBuilder(Constants.FRONT_LOGIN_USER_BEAN_SESSION_KEY);
        cacheKey.append(":");
        cacheKey.append(Constants.PLATFORM_TYPE_H5);
        cacheKey.append(":");
        cacheKey.append(uName);// 客户id
        return cacheKey.toString();
    }
    
    //查询memcached，判断用户是否已登录(同时,若存在则不是第一次登陆)
    public boolean isUserLoginKeyExists(String userLoginKey){
        /*FrontLoginUserBean userBean = memcachedClient.get(userLoginKey);
        if(userBean == null){
            return false;
        }
        return true;*/
        return false;

    }
    
    //保存登陆信息到memcached
    public void saveLoginToRedis(String userLoginKey,FrontLoginUserBean userToken){
        //memcachedClient.set(userLoginKey,userToken);
    }
    

    /**
     * 登陆校验
     * */
	public boolean isLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session == null){
			return false;
		}else{
            FrontLoginUserBean userLoginBean = (FrontLoginUserBean) session.getAttribute(Constants.FRONT_LOGIN_USER_BEAN_SESSION_KEY);
			if(userLoginBean == null){
				return false;
			}else{
			    //session中用户信息，放到localcontext中
                LocalContext.setFrontLoginUserBean(userLoginBean);
                return true;
            }
		}
		
	}
	
}
