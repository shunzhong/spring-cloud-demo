package com.learn.springcloud.consumer;

import com.learn.springcloud.dubbo.demo.SpringCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author shunzhong.deng
 * @version 1.0
 * @date 7/9/18 6:55 AM
 **/
@RestController
public class SpringCloudConsumerService {
    @Autowired
    private SpringCloudService springCloudService;

    @GetMapping("callDubboBySpringCloud")
    public String callDubboBySpringCloud() {

        return  springCloudService.sayHelloCallDubbo("6666666");
    }
}

