package com.learn.springcloud.auth.exception;

public class ResourceNoContentException extends  RuntimeException {
    public ResourceNoContentException(String msg) {
        super(msg);
    }

    public ResourceNoContentException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
