package com.damu.febs.auth.configure;

import com.damu.febs.auth.properties.FebsAuthProperties;
import com.damu.febs.common.handler.FebsAccessDeniedHandler;
import com.damu.febs.common.handler.FebsAuthExceptionEntryPoint;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 要配置多个资源，只需要以逗号分隔即可。
 *
 * 最后修改febs-auth模块下的资源服务器配置类FebsResourceServerConfigure：
 */
@Configuration
@EnableResourceServer
public class FebsResourceServerConfigure extends ResourceServerConfigurerAdapter {

    @Autowired
    private FebsAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private FebsAuthExceptionEntryPoint exceptionEntryPoint;
    @Autowired
    private FebsAuthProperties properties;

    //通过requestMatchers().antMatchers("/**")的配置表明该安全配置对所有请求都生效。
    // 类上的@EnableResourceServer用于开启资源服务器相关配置。
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //下面部分已经注释的是图像验证前
//        http.csrf().disable()
//                .requestMatchers().antMatchers("/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/**").authenticated();

        //configure(HttpSecurity http)方法里，我们通过.antMatchers(anonUrls).permitAll()配置了免认证资源，anonUrls为免认证资源数组，是从FebsAuthProperties配置中读取出来的值经过逗号分隔后的结果。
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

        http.csrf().disable()
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers("/**").authenticated()
                .and().httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}
