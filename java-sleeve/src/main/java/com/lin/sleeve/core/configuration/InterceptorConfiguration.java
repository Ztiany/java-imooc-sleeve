package com.lin.sleeve.core.configuration;

import com.lin.sleeve.core.interceptors.PermissionInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2021/1/26 18:58
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    /** 将  PermissionInterceptor 加入 IOC 容器中，才能才能完成对 PermissionInterceptor 的注入。 */
    @Bean
    public HandlerInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        getPermissionInterceptor() 这个方法调用拿到的其实是已经被完成注入的 PermissionInterceptor。
        否则 PermissionInterceptor 上的 userService 成员将会是 null 的。
        */
        registry.addInterceptor(getPermissionInterceptor());
    }

}
