package com.learn.springcloud.auth.exception;

/**
 * 参数为空异常
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable cause){
        super(msg, cause);
    }

    public static  BadRequestException  getInstance(String msg) {
        return new BadRequestException(msg);
    }
}
