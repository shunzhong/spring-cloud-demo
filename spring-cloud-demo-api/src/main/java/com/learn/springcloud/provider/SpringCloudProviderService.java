/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.learn.springcloud.provider;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 为 spring cloud dubbo 服务提供远程spring cloud 服务
 * @author shunzhong.deng
 * @date 6/28/18 2:26 PM
 * @version 1.0
 **/
@FeignClient(name = "spring-cloud-provider-demo")
public interface SpringCloudProviderService {

    /**
     * 说你好
     * @param name 名称
     * @return
     */
    @GetMapping("sayHello/{name}")
    String sayHello(@PathVariable("name") String name);


}