package com.learn.springcloud.auth.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;


/**
 *  spring cloud demo consumer
 * @author shunzhong.deng
 * @date 6/28/18 2:26 PM
 * @version 1.0
 **/
@SpringBootApplication
@EnableOAuth2Sso
public class SpringCloudAuthClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAuthClientApplication.class, args);
    }

}
