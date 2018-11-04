package com.water.component.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class WebSecurityInterceptor implements HandlerInterceptor{
	
	@Autowired
	private WebSecurityManager webSecurityManager;
	
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		LocalContext.clear();
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LocalContext.clear();
        boolean flag = webSecurityManager.isLogin(request);
        if(!flag){
        	//跳转到未登陆页面
        	response.sendRedirect("/5636-page/index");
        	return false;
        }
		return true;
	}

}
