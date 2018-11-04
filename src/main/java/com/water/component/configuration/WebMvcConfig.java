package com.water.component.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.water.intercept.DefaltIntercept;

/**
 * Created by liliang on 2017/10/10.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

    @Autowired
    WebSecurityManager webSecurityManager;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	//域名默认首页设置
        registry.addViewController("/").setViewName("forward:/index/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }


    @Bean
    public DefaltIntercept defaltIntercept() {
        return new DefaltIntercept();
    }

	@Bean
    public WebSecurityInterceptor webSecurityInterceptor() {
        return new WebSecurityInterceptor();
    }
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则,Sting...
        // excludePathPatterns 用户排除拦截,String...
        registry.addInterceptor(defaltIntercept()).addPathPatterns("/**");

        //登录验证拦截器---排除登录和注册相关接口
        registry.addInterceptor(webSecurityInterceptor()).addPathPatterns("/**","/5636-page/**").excludePathPatterns("/login/**","/appapi/**","/5636-page/index","/5636-page/login");

        super.addInterceptors(registry);

    }
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(true).setUseTrailingSlashMatch(true);
		super.configurePathMatch(configurer);
	}
}
