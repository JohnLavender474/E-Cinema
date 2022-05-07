package com.ecinema.app.configs;

import com.ecinema.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.ecinema.app.domain.enums.UserRole.*;

/**
 * https://www.baeldung.com/spring-security-login
 * https://www.baeldung.com/spring-security-custom-authentication-failure-handler
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] ANY_PERMITTED = new String[]{
            "/",
            "/about",
            "/change-password",
            "/change-password-confirm/**",
            "/error",
            "/index",
            "/login",
            "/login-error",
            "/message-page",
            "/movie-info/**",
            "/movie-reviews/**",
            "/movie-screenings/**",
            "/movies",
            "/register",
            "/screening/**",
            "/perform-login"
    };
    private static final String[] AUTHENTICATED_PERMITTED = new String[]{
            "/account",
            "/logout",
            "/logout-success",
    };
    private static final String[] CUSTOMERS_PERMITTED = new String[]{
            "/customer"
    };
    private static final String[] MODERATORS_PERMITTED = new String[]{
            "/moderator"
    };
    private static final String[] ADMINS_PERMITTED = new String[]{
            "/admin",
            "/add-movie",
            "/add-screening",
            "/add-showroom",
            "/edit-movie",
            "/edit-screening",
            "/edit-showroom",
            "/delete-movie",
            "/delete-screening",
            "/delete-showroom"
    };

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Authentication provider dao authentication provider.
     *
     * @return the dao authentication provider
     */
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
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/img/**", "/js/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)
            throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(ADMINS_PERMITTED).hasAuthority(ADMIN.getAuthority())
                .antMatchers(MODERATORS_PERMITTED).hasAuthority(MODERATOR.getAuthority())
                .antMatchers(CUSTOMERS_PERMITTED).hasAuthority(CUSTOMER.getAuthority())
                .antMatchers(AUTHENTICATED_PERMITTED).hasAnyAuthority(
                        CUSTOMER.getAuthority(), MODERATOR.getAuthority(), ADMIN.getAuthority())
                .antMatchers(ANY_PERMITTED).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .loginPage("/login")
                .loginProcessingUrl("/perform-login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/login-success", true)
                .failureUrl("/login-error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/logout-success")
                .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400);
    }

}
