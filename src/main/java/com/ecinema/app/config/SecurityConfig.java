package com.ecinema.app.config;

import com.ecinema.app.services.UserService;
import com.ecinema.app.utils.beans.PasswordEncryptor;
import com.ecinema.app.utils.constants.UrlPermissions;
import com.ecinema.app.utils.constants.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)
    throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(UrlPermissions.CUSTOMER).hasRole(UserRole.CUSTOMER.getAuthority())

                // TODO: Add admin, admin trainee, and moderator permissions

                .antMatchers(UrlPermissions.ANY).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.jsp")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/homepage.jsp", true)
                .failureUrl("/login.html?error=true")
                .failureHandler(null) // TODO: add authentication failure handler
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(null); // TODO: add logout success handler
    }

}
