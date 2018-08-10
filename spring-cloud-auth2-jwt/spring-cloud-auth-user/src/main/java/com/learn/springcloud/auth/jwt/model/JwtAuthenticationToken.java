package com.learn.springcloud.auth.jwt.model;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


/**
 * 保存token
 * @author shunzhong.deng
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    /**
     * 存放具体的token 数据
     */
    private String token;

    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}