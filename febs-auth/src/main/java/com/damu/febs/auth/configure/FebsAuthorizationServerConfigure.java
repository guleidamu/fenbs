package com.damu.febs.auth.configure;

import com.damu.febs.auth.properties.FebsAuthProperties;
import com.damu.febs.auth.properties.FebsClientsProperties;
import com.damu.febs.auth.service.FebsUserDetailService;
import com.damu.febs.auth.translator.FebsWebResponseExceptionTranslator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 认证服务器相关的安全配置类
 */
//@EnableAuthorizationServer注解标注，开启认证服务器相关配置。
@Configuration
@EnableAuthorizationServer
public class FebsAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private FebsUserDetailService userDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FebsAuthProperties authProperties;

    @Autowired
    private FebsWebResponseExceptionTranslator exceptionTranslator;

    //该方法主要配置了：
    //客户端从认证服务器获取令牌的时候，必须使用client_id为febs，client_secret为123456的标识来获取；
    //该client_id支持password模式获取令牌，并且可以通过refresh_token来获取新的令牌；
    //在获取client_id为febs的令牌的时候，scope只能指定为all，否则将获取失败
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("febs")
//                .secret(passwordEncoder.encode("123456"))
//                .authorizedGrantTypes("password", "refresh_token")
//                .scopes("all");
//    }
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        FebsClientsProperties[] clientsArray = authProperties.getClients();
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(clientsArray)) {
            for (FebsClientsProperties client : clientsArray) {
                if (StringUtils.isBlank(client.getClient())) {
                    throw new Exception("client不能为空");
                }
                if (StringUtils.isBlank(client.getSecret())) {
                    throw new Exception("secret不能为空");
                }
                String[] grantTypes = StringUtils.splitByWholeSeparatorPreserveAllTokens(client.getGrantType(), ",");
                builder.withClient(client.getClient())
                        .secret(passwordEncoder.encode(client.getSecret()))
                        .authorizedGrantTypes(grantTypes)
                        .scopes(client.getScope());
            }
        }
    }


//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        endpoints.tokenStore(tokenStore())
//                .userDetailsService(userDetailService)
//                .authenticationManager(authenticationManager)
//                .tokenServices(defaultTokenServices());
//    }

    @Override
    @SuppressWarnings("all")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager)
                .tokenServices(defaultTokenServices())
                .exceptionTranslator(exceptionTranslator);//异常处理翻译器
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    //defaultTokenServices指定了令牌的基本配置
//    @Primary
//    @Bean
//    public DefaultTokenServices defaultTokenServices() {
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(tokenStore());
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24);
//        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
//        return tokenServices;
//    }

    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(authProperties.getAccessTokenValiditySeconds());
        tokenServices.setRefreshTokenValiditySeconds(authProperties.getRefreshTokenValiditySeconds());
        return tokenServices;
    }


}
