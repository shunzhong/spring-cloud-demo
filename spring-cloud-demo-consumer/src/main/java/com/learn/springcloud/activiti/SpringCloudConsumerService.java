package com.learn.springcloud.activiti;

import com.learn.springcloud.provider.SpringCloudProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private SpringCloudProviderService springCloudProviderService;

    @GetMapping("sayHelloConsumer/{name}")
    public String callDubboBySpringCloud(@PathVariable("name") String name) {

        return  springCloudProviderService.sayHello(name);
    }
}

