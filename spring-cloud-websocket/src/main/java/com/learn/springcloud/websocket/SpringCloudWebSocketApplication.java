package com.learn.springcloud.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *  spring cloud demo consumer
 * @author shunzhong.deng
 * @date 6/28/18 2:26 PM
 * @version 1.0
 **/
@SpringBootApplication
@EnableHystrix
@EnableDiscoveryClient
public class SpringCloudWebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudWebSocketApplication.class, args);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
