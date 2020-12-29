package com.damu.febs.auth.configure;

import com.damu.febs.auth.filter.ValidateCodeFilter;
import com.damu.febs.auth.service.FebsUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * FebsSecurityConfigure用于处理/oauth开头的请求，Spring Cloud OAuth内部定义的获取令牌，刷新令牌的请求地址都是以/oauth/开头的，
 * 也就是说FebsSecurityConfigure用于处理和令牌相关的请求；
 */
//EnableWebSecurity注解有两个作用,1: 加载了WebSecurityConfiguration配置类, 配置安全认证策略。
// 2: 加载了AuthenticationConfiguration, 配置了认证信息。
//可以点击查看
@Order(2)
@EnableWebSecurity
public class FebsSecurityConfigure extends WebSecurityConfigurerAdapter {


    @Autowired
    private FebsUserDetailService userDetailService;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //requestMatchers().antMatchers("/oauth/**")的含义是：FebsSecurityConfigure安全配置类只对/oauth/开头的请求有效。
//        http.requestMatchers()
//                .antMatchers("/oauth/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated()
//                .and()
//                .csrf().disable();
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .requestMatchers()
                .antMatchers("/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .csrf().disable();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .requestMatchers()
//                .antMatchers("/oauth/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated()
//                .and()
//                .csrf().ignoringAntMatchers("/oauth/**")
//                .and()
//                .authorizeRequests().antMatchers("/actuator/**").permitAll();
//        super.configure(http);
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }
}
