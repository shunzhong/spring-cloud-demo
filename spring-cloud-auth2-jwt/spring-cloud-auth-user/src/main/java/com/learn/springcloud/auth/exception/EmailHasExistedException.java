package com.learn.springcloud.auth.exception;

/**
 * 参数的数据格式
 */
public class EmailHasExistedException extends RuntimeException {

    public EmailHasExistedException(String msg) {
        super(msg);
    }

    public EmailHasExistedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
