package com.learn.springcloud.consumer;

import com.learn.springcloud.dubbo.demo.SpringCloudService;
import com.learn.springcloud.provider.SpringCloudProviderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *  spring cloud demo consumer
 * @author shunzhong.deng
 * @date 6/28/18 2:26 PM
 * @version 1.0
 **/
@SpringBootApplication
@EnableHystrix
@EnableFeignClients(clients = {SpringCloudProviderService.class, SpringCloudService.class})
@EnableDiscoveryClient
public class SpringCloudConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConsumerApplication.class, args);
    }
}
