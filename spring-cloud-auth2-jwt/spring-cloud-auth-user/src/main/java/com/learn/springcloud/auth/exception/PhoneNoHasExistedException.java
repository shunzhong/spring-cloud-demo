package com.learn.springcloud.auth.exception;

/**
 * 参数的数据格式
 */
public class PhoneNoHasExistedException extends RuntimeException {

    public PhoneNoHasExistedException(String msg) {
        super(msg);
    }

    public PhoneNoHasExistedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
