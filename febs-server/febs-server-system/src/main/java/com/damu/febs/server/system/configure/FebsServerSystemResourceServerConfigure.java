package com.damu.febs.server.system.configure;

import com.damu.febs.server.system.properties.FebsServerSystemProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class FebsServerSystemResourceServerConfigure extends ResourceServerConfigurerAdapter {

    //这个给添加，否则UserServiceImpl报找不到bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private FebsServerSystemProperties properties;

//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .requestMatchers().antMatchers("/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/**").authenticated();
//    }

    //资源服务器配置类里添加免认证路径配置
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

        http.csrf().disable()
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers("/**").authenticated();
    }
}