package com.water.component.configuration;

import com.water.modules.model.FrontLoginUserBean;


public class LocalContext {
	
	private static ThreadLocal<FrontLoginUserBean> loginThreadLocal = new ThreadLocal<FrontLoginUserBean>();

    public static FrontLoginUserBean getFrontLoginUserBean(){
    	return loginThreadLocal.get();
    }
    public static void setFrontLoginUserBean(FrontLoginUserBean value)
    {
        loginThreadLocal.set(value);
    }
    public static Integer getuUserId()
    {
        if (null != loginThreadLocal.get())
        {
            return loginThreadLocal.get().getUserid();
        }
        return null;
    }
    public static String getuLoginId()
    {
        if (null != loginThreadLocal.get())
        {
            return loginThreadLocal.get().getUsername();
        }
        return null;
    }
    
    public static void clear()
    {
        loginThreadLocal.remove();
    }
    
    

}
