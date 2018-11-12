package com.learn.springcloud.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class Main {
    @RequestMapping("/")
    public String home(Principal user) {
        return "Hello " + user.getName();
    }

}
