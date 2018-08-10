package com.learn.springcloud.provider.service;

import com.learn.springcloud.provider.SpringCloudProviderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对spring cloud dubbo 项目提供远程 spring cloud 服务
 * @author shunzhong.deng
 * @version 1.0
 * @date 7/8/18 10:17 PM
 **/
@RestController
public class SpringCloudProviderServiceImpl implements SpringCloudProviderService {
    @Override
    public String sayHello(@PathVariable("name")  String name) {
        return "spring cloud provider " + name;
    }
}
