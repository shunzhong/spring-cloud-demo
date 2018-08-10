package com.learn.springcloud.auth.signature;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否需要验证签名,加了注解就是忽略签名校验
 * 用在类上面表示该类所有方法不进行签名校验
 * 用在方法上面表示该方法不进行签名校验
 *
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreSignature {

}
