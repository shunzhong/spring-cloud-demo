package com.learn.springcloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security Config
 *
 * @author shunzhong.deng
 * @version 1.0
 * @date 7/19/18 4:04 PM
 **/
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    /**
     * It allows configuring web-based security for specific http requests.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().formLogin();

        // 由于使用jwt，所以不需要 csrf
        http.csrf().disable()
                .authorizeRequests()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
    }
    /**
     * The place to configure the default authenticationManager @Bean.
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication() // creating user in memory
                .withUser("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER", "TRUSTED_CLIENT")

                .and()

                .withUser("admin")
                .password(passwordEncoder().encode("password"))
                .authorities("ROLE_ADMIN", "ROLE_TRUSTED_CLIENT");
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // provides the default AuthenticationManager as a Bean
        return super.authenticationManagerBean();
    }


    /**
     * Allows modifying and accessing the UserDetailsService, a core interface which loads user-specific data.
     *
     * @return
     */
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
