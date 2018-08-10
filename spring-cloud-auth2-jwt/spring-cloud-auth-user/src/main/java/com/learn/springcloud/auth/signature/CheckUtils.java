package com.learn.springcloud.auth.signature;
import java.io.UnsupportedEncodingException;
import java.util.*;


import com.learn.springcloud.auth.signature.exception.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
/**
 * 校验签名是否有效的工具类
 *
 */
public class CheckUtils {

    private static final String SIGN_PARAMETER_KEY = "sign";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA384withRSA";

    /**
     * 验证参数签名
     * @param requestParameterMap 请求参数列表
     * @return 验证结果
     * @throws SignatureException 签名异常
     */
    public static boolean checkRSASign(Map<String, Object> requestParameterMap,
                                       String publicKey) throws SignatureException {
        if (StringUtils.isEmpty(publicKey)) {
            throw new SignatureException("RSA 公钥配置为空");
        }

        // 客户端传递过来的签名（客户端使用私钥加密的数据）
        String sign = String.valueOf(requestParameterMap.get(SIGN_PARAMETER_KEY));
        if (StringUtils.isEmpty(sign)) {
           throw new SignatureException("sign 参数为空");
        }

        try {
            // 生成参数指纹,对字符串排序后生成md5值
            String md5Code = DigestUtils.md5DigestAsHex(getSignData(requestParameterMap).getBytes("UTF-8"));

            if (StringUtils.isEmpty(md5Code)) {
                throw  new SignatureException("参数指纹生成错误");
            }
            return RSAUtils.verify(md5Code.getBytes(), publicKey, sign, SIGNATURE_ALGORITHM);
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("签名异常，请求参数编码异常");
        } catch (java.security.SignatureException e) {
            e.printStackTrace();
            throw new SignatureException("签名异常，请求参数编码异常");
        }

    }


    /**
     * 获取待签名串
     * 1、将所有参数（注意是所有参数），除去sign本身，以及值是空的参数，按参数名字母升序排序。
     * 2、然后将请求参数拼接成如下形式：key=value&key1=value&key2=value2
     * @param requestParameterMap 请求参数列表
     * @return 返回待验证的数据
     */
    public static String getSignData(Map<String, Object> requestParameterMap) {
        StringBuffer content = new StringBuffer();

        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<>(requestParameterMap.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (SIGN_PARAMETER_KEY.equals(key)) {
                continue;
            }
            Object value = requestParameterMap.get(key);
            // 空串不参与签名
            if (null == value || StringUtils.isEmpty(String.valueOf(value))) {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);

        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&")) {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }

}
