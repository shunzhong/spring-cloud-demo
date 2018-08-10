package com.learn.springcloud.auth.jwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when token cannot be parsed
 * @author shunzhong.deng
 */
public class JwtTokenMalformedException extends AuthenticationException {


    public JwtTokenMalformedException(String msg) {
        super(msg);
    }

    public JwtTokenMalformedException(String message,Throwable cause) {
        super(message, cause);
    }
}
