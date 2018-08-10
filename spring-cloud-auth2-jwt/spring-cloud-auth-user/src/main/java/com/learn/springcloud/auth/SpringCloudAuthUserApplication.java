package com.learn.springcloud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 用户鉴权
 *
 * @author shunzhong.deng
 * @version 1.0
 * @date 8/10/18 3:50 PM
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class SpringCloudAuthUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAuthUserApplication.class, args);
    }

}