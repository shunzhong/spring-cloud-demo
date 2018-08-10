package com.learn.springcloud.auth.signature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.learn.springcloud.auth.exception.BadRequestException;
import com.learn.springcloud.auth.exception.ParameterFormatNotSupportedException;
import com.learn.springcloud.auth.signature.exception.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 校验签名的切面类
 */
//@Aspect
//@Component
public class SignatureAspect {
    /**
     * 请求超时是否返回时间戳
     */
    @Value("${IS_RETURN_TIMESTAMP}")
    private String IS_RETURN_TIMESTAMP;

    /**
     * 获取私钥
     */
    @Value("${RSA_PRIVATE_KEY_IOS}")
    private String RSA_PRIVATE_KEY_IOS;
    /**
     * 获取请求超时时间
     */
    @Value("${REQUEST_EXPIRATION}")
    private String REQUEST_EXPIRATION;
    /**
     * 获取公钥
     */
    @Value("${RSA_PUBLIC_KEY_IOS}")
    private String RSA_PUBLIC_KEY_IOS; //ios

    @Value("${RSA_PUBLIC_KEY_ANDROID}")
    private String RSA_PUBLIC_KEY_ANDROID;  //android

    @Value("${RSA_PUBLIC_KEY_PC}")
    private String RSA_PUBLIC_KEY_PC;  //pc

    @Value("${RSA_PUBLIC_KEY_WECHAT}")
    private String RSA_PUBLIC_KEY_WECHAT;  //微信


    Logger logger = LoggerFactory.getLogger(SignatureAspect.class);

    /**
     * 默认对com.ycs.api.controller 包下所有Handler进行签名验证
     */
    @Pointcut("execution(* com.ycs.api.controller..*(..))")
    public void signPointcut() {
    }

    /**
     * @param joinPoint 当前的切点对象
     * @return
     * @throws Throwable
     */
    @Around("signPointcut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1.如果类上面加了忽略签名注解，该类所有方法都不进行签名校验
        Class<?> clzz = joinPoint.getTarget().getClass();
        IgnoreSignature annotation = clzz.getAnnotation(IgnoreSignature.class);
        if (annotation != null) {
            logger.warn("类[{}]存在忽略签名的注解", clzz.getName());
            return joinPoint.proceed();
        }

        // 2.从切点上获取目标方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 3.若目标方法加了忽略签名的注解，直接调用不进行签名校验,连连回调时时没有tiamstamp这些参数的,所以校验请求时间的判断下面才开始
        if (method.isAnnotationPresent(IgnoreSignature.class)) {
            logger.warn("方法[{}]存在忽略签名的注解", clzz.getName() + "#" + method.getName());
            return joinPoint.proceed();
        }

        // 校验请求发送时间，防止重放攻击
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        if (null == request) {
            throw new SignatureException("系统错误，请求参数获取错误");
        }
        Map params = request.getParameterMap();
        if (null == params || params.size() == 0) {
            throw new BadRequestException("请求参数为空");
        }

        // 获取请求的appKey为了下面校验签名获取对应的公钥
        String appKey = request.getParameter("appKey");
        // 获取请求时间戳,防重放攻击
        String requestTimeString = request.getParameter("timestamp");   //请求时间
        isBadRequestTimestamp(requestTimeString);

        // 4.没有忽略签名的进行签名校验

        Map<String, Object> requestMap = new HashMap<>(params.size());

        Iterator it = params.keySet().iterator();
        String paramName;
        String paramValue;
        while (it.hasNext()) {
            paramName = (String) it.next();
            paramValue = request.getParameter(paramName);
            if (!StringUtils.isEmpty(paramName) && !StringUtils.isEmpty(paramValue)) {
                requestMap.put(paramName, paramValue);
            }
            if (requestMap.size() == 0) {
                throw new SignatureException("要求的验证签名的参数错误");
            }

            /// 第一阶段先调试 RSA签名，第二阶段开始RSA请求参数加密或者Https
            /// 暂时关闭RSA请求加密解密功能
            // RSA 加密请求解密
        /*if (null != requestMap.get("encryptStr")) {
            String encryptStr = String.valueOf(requestMap.get("encryptStr"));
            logger.info("decrypt request[{}]",
            new String(RSAUtils.decryptByPrivateKey(Base64.getDecoder().decode(encryptStr),
            RSA_PRIVATE_KEY)));
        }*/

            // 2、验证当前设备IMEI是否在黑名单中


            // 3、校验签名,如果通过则放行
        }
        try {
            // 签名校验成功
            if (CheckUtils.checkRSASign(requestMap, getRsaPublicKEy(appKey))) {
                // 继续往下执行，并将执行的结果返回
                return joinPoint.proceed();
            }
            // 4、 存入黑名单，并终止结束请求
            throw new SignatureException("签名校验失败");
        } catch (SignatureException signatureException) {
            throw signatureException;
        }
    }

    /**
     * 校验请求的时间戳是否是非法请求,
     *
     * @param requestTimestampString 请求的timestamp值
     * @return true 表示非法请求
     */
    private void isBadRequestTimestamp(String requestTimestampString) {
        if (requestTimestampString == null) {
            throw new BadRequestException("timestamp为空");
        }
        if (!NumberUtils.isNumber(requestTimestampString)) {
            throw new ParameterFormatNotSupportedException("timestamp格式错误");
        }

        String expirationTimeString = REQUEST_EXPIRATION == null ? "300" : REQUEST_EXPIRATION;

        Long nowTime = System.currentTimeMillis();  //当前系统时间
        Long requestTime = Long.parseLong(requestTimestampString);
        Long expirationTime = Long.parseLong(expirationTimeString) * 1000;

        if (requestTime > nowTime || nowTime - requestTime > expirationTime) {
            // 如果是dev或者test环境，返回当前时间戳，便于测试
            if (IS_RETURN_TIMESTAMP != null && "true".equals(IS_RETURN_TIMESTAMP)) {
                throw new SignatureException("请求超时," + nowTime);
            }
            throw new SignatureException("请求超时");
        }
    }

    /**
     * 根据appKey 获取对应的公钥
     *
     * @param appKeyString //客户端类型， 各客户端取值如下，pc：100、安卓：101、IOS:102、微信端：103
     * @return
     */
    private String getRsaPublicKEy(String appKeyString) {
        if (appKeyString == null) {
            throw new BadRequestException("appKey参数为空");
        }
        if (!NumberUtils.isNumber(appKeyString)) {
            throw new ParameterFormatNotSupportedException("appKey格式错误");
        }
        int appKey = NumberUtils.toInt(appKeyString);

        String RsaPublicKey;
        switch (appKey) {
            case 100:
                RsaPublicKey = RSA_PUBLIC_KEY_PC;
                break;
            case 101:
                RsaPublicKey = RSA_PUBLIC_KEY_ANDROID;
                break;
            case 102:
                RsaPublicKey = RSA_PUBLIC_KEY_IOS;
                break;
            case 103:
                RsaPublicKey = RSA_PUBLIC_KEY_WECHAT;
                break;
            default:
                RsaPublicKey = null;
        }
        if (RsaPublicKey == null) {
            throw new ParameterFormatNotSupportedException("appKey格式错误");
        }

        return RsaPublicKey;
    }
}
