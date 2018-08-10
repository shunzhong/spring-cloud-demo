package com.learn.springcloud.auth.exception;

/**
 * 参数的数据格式
 */
public class ParameterFormatNotSupportedException extends RuntimeException {

    public ParameterFormatNotSupportedException(String msg) {
        super(msg);
    }

    public ParameterFormatNotSupportedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
