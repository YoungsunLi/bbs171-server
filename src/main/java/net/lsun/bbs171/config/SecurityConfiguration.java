package net.lsun.bbs171.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 不需要 token 的 url
    public static final String[] AUTH_WHITELIST = {
            "/user/token",
            "/user/send_code",
            "/user/send_code_exist",
            "/user/reg",
            "/user/reset_password",

            "/post/get_posts_for_index"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 可以匿名访问的链接
                .antMatchers(AUTH_WHITELIST).permitAll()
                // 其他所有请求需要身份认证
                .anyRequest().authenticated().and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }
}
