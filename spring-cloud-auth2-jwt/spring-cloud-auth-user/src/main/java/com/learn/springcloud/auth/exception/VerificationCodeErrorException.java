package com.learn.springcloud.auth.exception;

/**
 * 验证码校验不通过
 * 返回json如下
 * {
 * "resCode": "VerificationCodeError",
 * "resDesc": "验证码错误",
 * "exception": null,
 * "data": null
 * }
 */
public class VerificationCodeErrorException extends RuntimeException {
    public VerificationCodeErrorException() {
    }
}
