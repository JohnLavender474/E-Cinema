package com.ecinema.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * https://github.com/eugenp/tutorials/blob/master/spring-security-modules/
 * spring-security-web-mvc-custom/src/main/java/com/baeldung/spring/MvcConfig.java
 */
@EnableWebMvc
@Configuration
@ComponentScan("com.ecinema.app.controllers")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/about.html");
        registry.addViewController("/admin.html");
        registry.addViewController("/admin_trainee.html");
        registry.addViewController("/authenticated.html");
        registry.addViewController("/customer.html");
        registry.addViewController("/index");
        registry.addViewController("/login");
        registry.addViewController("/logout.html");
        registry.addViewController("/moderator.html");
        registry.addViewController("/profile.html");
        registry.addViewController("/welcome.html");
    }

    /**
     * View resolver view resolver.
     *
     * @return the view resolver
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/templates");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO: add interceptors
    }

}
