package com.learn.springcloud.auth.exception;

/**
 * 邮件发送失败
 */
public class SendPhoneCodeFailedException extends RuntimeException {

    public SendPhoneCodeFailedException(String msg) {
        super(msg);
    }

    public SendPhoneCodeFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
