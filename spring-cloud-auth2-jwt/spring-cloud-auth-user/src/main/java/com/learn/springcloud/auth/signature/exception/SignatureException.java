package com.learn.springcloud.auth.signature.exception;

/**
 * 签名校验异常
 */
public class SignatureException extends RuntimeException {

    /**
     *
     * @param message
     */
    public SignatureException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public SignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
