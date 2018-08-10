package com.learn.springcloud.auth.exception;

/**
 * 参数为空异常
 */
public class SystemErrorException extends RuntimeException {

    public SystemErrorException(String msg) {
        super(msg);
    }

    public SystemErrorException(String msg, Throwable cause){
        super(msg, cause);
    }

    public static SystemErrorException getInstance(String msg) {
        return new SystemErrorException(msg);
    }
}
