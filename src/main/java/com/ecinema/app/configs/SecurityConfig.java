package com.ecinema.app.configs;

import com.ecinema.app.beans.AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.ecinema.app.domain.enums.UserAuthority.*;

/**
 * https://www.baeldung.com/spring-security-login
 * https://www.baeldung.com/spring-security-custom-authentication-failure-handler
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] ANY_PERMITTED = new String[]{
            "/about/**",
            "/change-password/**",
            "/change-password-confirm/**",
            "/confirm-registration/**",
            "/error/**",
            "/get-email-for-change-password/**",
            "/index/**",
            "/login/**",
            "/login-error/**",
            "/post-email/**",
            "/message-page/**",
            "/movie-info/**",
            "/movie-reviews/**",
            "/movie-screenings/**",
            "/movies/**",
            "/perform-login/**",
            "/post-email/**",
            "/submit-registration/**",
            "/screening/**"
    };
    private static final String[] AUTHENTICATED_PERMITTED = new String[]{
            "/logout/**",
            "/logout-success/**",
            "/user-profile/**",
            "/edit-user-profile/**"
    };
    private static final String[] CUSTOMERS_PERMITTED = new String[]{
            "/customer/**",
            "/write-review/**"
    };
    private static final String[] MODERATORS_AND_ADMINS_PERMITTED = new String[]{
            "/management/**"
    };
    private static final String[] MODERATORS_PERMITTED = new String[]{

    };
    private static final String[] ADMINS_PERMITTED = new String[]{
            "/add-movie/**",
            "/add-screening/**",
            "/add-showroom/**",
            "/admin-movie-choose/**",
            "/edit-movie/**",
            "/edit-movie-search/**",
            "/edit-screening/**",
            "/edit-showroom/**",
            "/delete-movie/**",
            "/delete-movie-search",
            "/delete-screening/**",
            "/delete-showroom/**",
            "/manage-user-accounts/**"
    };

    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(authenticationProvider.daoAuthenticationProvider());
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
                .antMatchers(MODERATORS_AND_ADMINS_PERMITTED).hasAnyAuthority(
                        MODERATOR.getAuthority(), ADMIN.getAuthority())
                .antMatchers(CUSTOMERS_PERMITTED).hasAuthority(CUSTOMER.getAuthority())
                .antMatchers(AUTHENTICATED_PERMITTED).authenticated()
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
