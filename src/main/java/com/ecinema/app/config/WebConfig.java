package com.ecinema.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/
 * spring-security-web-mvc-custom/src/main/java/com/baeldung/spring/MvcConfig.java
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.addViewController("/admin").setViewName("admin");
        // registry.addViewController("/trainee").setViewName("trainee");
        // registry.addViewController("/authenticated").setViewName("authenticated");
        // registry.addViewController("/customer").setViewName("customer");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
        // registry.addViewController("/logout").setViewName("logout");
        // registry.addViewController("/moderator").setViewName("moderator");
        // registry.addViewController("/profile").setViewName("profile");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO: add interceptors
    }

}
