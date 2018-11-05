package com.learn.springcloud.consumer;

import com.learn.springcloud.provider.SpringCloudProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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


    /**
     * 优雅关机测试
     * 1、 	$ curl -i localhost:8080/long-process
     * 2、 kill pid
     * 3、 expect result Process finished
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/long-process")
    public String pause() throws InterruptedException {
        Thread.sleep(10000);
        return "Process finished";
    }



}

