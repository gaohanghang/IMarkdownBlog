package com.my.blog.website.interceptor;


import com.my.blog.website.utils.TaleUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 *  向mvc中添加自定义组件
 *
 *  spring boot配置类
 *  WebMvcConfigurerAdapter配置类其实是Spring内部的一种配置方式，采用JavaBean的形式来
 *  代替传统的xml配置文件形式进行针对框架个性化定制，下面我们来看一下该类内的常用方法。
 *
 *  继承WebMvcConfigurerAdapter采用JavaBean形式实现个性化配置定制。
 */
@Component
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Resource
    private BaseInterceptor baseInterceptor; // 注入baseInterceptor实例对象

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor);
    }

    /**
     * 添加静态资源文件，外部可以直接访问地址
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ TaleUtils.getUplodFilePath()+"upload/");
        super.addResourceHandlers(registry);
    }
}
