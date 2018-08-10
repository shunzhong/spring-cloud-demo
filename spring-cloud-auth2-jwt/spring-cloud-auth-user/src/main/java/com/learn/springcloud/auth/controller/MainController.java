package com.learn.springcloud.auth.controller;


import com.learn.springcloud.auth.jwt.util.JwtTokenUtils;
import com.learn.springcloud.auth.signature.CheckUtils;
import com.learn.springcloud.auth.signature.RSAUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.net.URLEncoder;
import java.security.Principal;
import java.security.Signature;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Example controller to test security calls
 */
@RestController
public class MainController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /**
     * 获取IOS公钥
     */
    @Value("${RSA_PUBLIC_KEY_IOS}")
    private String RSA_PUBLIC_KEY_IOS;


    /**
     * 获取IOS私钥
     */
    @Value("${RSA_PRIVATE_KEY_IOS}")
    private String RSA_PRIVATE_KEY_IOS;


    @Autowired
    private JwtTokenUtils jwtTokenUtils;


    /**
     * 普通 springmvc测试
     *
     * @param name 名称
     * @return
     */
    @RequestMapping(value = "/main/hello", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    /**
     * spring mvc + spring security 权限测试
     *
     * @param principal
     * @return
     */
    @RequestMapping(value = {"/main/user", "/me"}, method = RequestMethod.POST)
    public ResponseEntity<?> user(Principal principal) {

        // getRole 方法需要 user 权限
        return ResponseEntity.ok(principal);
    }

    /**
     * 用户授权登录，返回token
     *
     * @return
     */
    @RequestMapping(value = "/auth/logintest")
    public String login() {
        List<String> scopes = new ArrayList<>(1);
        scopes.add("USER");
        return jwtTokenUtils.generateToken("1222", scopes);
    }

    /**
     * 产生新的RSA私钥公钥
     *
     * @return
     */
    @RequestMapping(value = "/auth/getKey")
    public Map<String, String> generateRsaKeyPair() throws Exception {
        Map<String, String> keyPairMap = new HashMap<>(2);
        keyPairMap.putAll(RSAUtils.generateKeyPair(1024));
        return keyPairMap;
    }

    /**
     * 验证RSA 签名
     *
     * @param userName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkSign", method = RequestMethod.PUT)
    public Map<String, Object> checkSign(String userName) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("checkStatus", "true");
        resultMap.put("name", userName);
        return resultMap;
    }


    /**
     * 获取RSA签名
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/auth/getSign", method = RequestMethod.POST)
    public Map<String, Object> getSign() throws Exception {
        Map<String, Object> parameterMap = new HashMap<>(8);

        parameterMap.put("appKey", "1002");
        parameterMap.put("timestamp", "1510890244966");
        parameterMap.put("imei", "a144c21321c14e198f3955912431e698");
        parameterMap.put("appVersion", "201");

        parameterMap.put("userName", "zhangsan");
        parameterMap.put("age", "69");
        parameterMap.put("gender", "1");
        parameterMap.put("xxx", "0000");
        //parameterMap.put("_method", "put");
        // 生成参数指纹
        String md5Code = DigestUtils.md5DigestAsHex(CheckUtils.getSignData(parameterMap).getBytes());


        if (StringUtils.isEmpty(md5Code)) {
            throw new Exception("参数指纹生成错误");
        }

        // 对请求参数签名
        Signature signature = Signature.getInstance(CheckUtils.SIGNATURE_ALGORITHM);
        signature.initSign(RSAUtils.getPrivateKey(RSA_PRIVATE_KEY_IOS));
        signature.update(md5Code.getBytes());
        byte[] signBytes = signature.sign();

        // 验证签名算法
        Signature signatureValidator = Signature.getInstance(CheckUtils.SIGNATURE_ALGORITHM);
        signatureValidator.initVerify(RSAUtils.getPublicKey(RSA_PUBLIC_KEY_IOS));
        signatureValidator.update(md5Code.getBytes());
        boolean isValid = signatureValidator.verify(signBytes);


        String bBytesStr = new String(Base64.getEncoder().encode(signBytes));
        Map<String, Object> resultMap = new HashMap<>(12);
        resultMap.put("sign_url", URLEncoder.encode(bBytesStr, "utf-8"));
        resultMap.put("sign", bBytesStr);
        resultMap.put("md5Code", md5Code);
        resultMap.put("validatorResult", isValid);
        resultMap.putAll(parameterMap);

        // 需要加密的参数
        String encryptSrc = CheckUtils.getSignData(resultMap);

        byte[] encryptBytes = RSAUtils.encryptByPublicKey(encryptSrc.getBytes(), RSA_PUBLIC_KEY_IOS);
        resultMap.put("encryptStr", new String(Base64.getEncoder().encode(encryptBytes)));
        resultMap.put("decryptStr", new String(RSAUtils.decryptByPrivateKey(encryptBytes, RSA_PRIVATE_KEY_IOS)));
        return resultMap;
    }

}
