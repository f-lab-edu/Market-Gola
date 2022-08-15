package com.flab.marketgola.user.config;

import com.flab.marketgola.user.argumentresolver.LoginArgumentResolver;
import com.flab.marketgola.user.controller.LoginController;
import com.flab.marketgola.user.controller.UserController;
import com.flab.marketgola.user.intercepter.LoginCheckInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(LoginController.LOGIN_PATH, UserController.BASE_PATH);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(new LoginArgumentResolver());
    }
}
