package com.learn.springcloud.auth.client.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户管理
 * @author shunzhong.deng
 */
@RestController
public class UserController {
    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
