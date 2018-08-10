package com.learn.springcloud.auth.exception;

/**
 * 邮件发送失败
 */
public class SendEmailFailedException extends RuntimeException {

    public SendEmailFailedException(String msg) {
        super(msg);
    }

    public SendEmailFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
