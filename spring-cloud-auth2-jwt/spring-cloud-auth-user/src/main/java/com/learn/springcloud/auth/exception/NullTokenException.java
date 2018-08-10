package com.learn.springcloud.auth.exception;

/**
 * token为空的异常，在token放行路径下，必须要传token的接口要对token进行判空
 */
public class NullTokenException extends RuntimeException {

    public NullTokenException() {
    }
}
