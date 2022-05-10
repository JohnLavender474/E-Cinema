package com.ecinema.app.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/
 * spring-security-web-mvc-custom/src/main/java/com/baeldung/spring/MvcConfig.java
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/add-screenings").setViewName("add-screenings");
        registry.addViewController("/add-showrooms").setViewName("add-showrooms");
        registry.addViewController("/delete-movie").setViewName("delete-movie");
        registry.addViewController("/delete-screenings").setViewName("delete-screenings");
        registry.addViewController("/delete-showrooms").setViewName("delete-showrooms");
        registry.addViewController("/edit-movie").setViewName("edit-movie");
        registry.addViewController("/edit-screenings").setViewName("edit-screenings");
        registry.addViewController("/edit-showrooms").setViewName("edit-showrooms");
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login-success").setViewName("login-success");
        registry.addViewController("/logout-success").setViewName("logout-success");
        registry.addViewController("/manage-user-accounts").setViewName("manage-user-accounts");
        registry.addViewController("/message-page").setViewName("message-page");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

}
