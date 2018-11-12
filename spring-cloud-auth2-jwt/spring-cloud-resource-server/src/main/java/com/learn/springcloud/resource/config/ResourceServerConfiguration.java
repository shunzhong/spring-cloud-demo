package com.learn.springcloud.resource.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;


/**
 * OAuth2 Resource Server Config
 *
 * @author shunzhong.deng
 * @version 1.0
 * @date 7/19/18 4:03 PM
 **/
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(ResourceServerSecurityConfigurer resources)
            throws Exception {
        resources.resourceId("resource");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/foo", "/bar", "/hello", "/test", "/me", "/user").authenticated().and().csrf().disable();
    }

    @Bean
    public LocalRemoteTokenServices localTokenService() {
        final LocalRemoteTokenServices tokenService = new LocalRemoteTokenServices();
        // TODO: 7/30/18  可以通过负载均衡获取服务的IP地址
        tokenService.setCheckTokenEndpointUrl("http://localhost:9081/oauth/check_token");
        tokenService.setClientId("my-client");
        tokenService.setClientSecret("mysecret");
        tokenService.setAccessTokenConverter(accessTokenConverter());
        return tokenService;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource("mykeys.jks"), "mypass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mykeys"));
        return converter;
    }

}