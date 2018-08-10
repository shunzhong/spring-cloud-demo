package com.learn.springcloud.auth.signature;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来加密对称加密的密钥，从而保证秘钥的安全以及保证数据的安全
 *
 * 签名与验签
 * 加密与解密
 *
 * </p>
 */
public class RSAUtils {

    static Logger logger = LoggerFactory.getLogger(RSAUtils.class);
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception{
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception{
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, String> generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, String> keyMap = new HashMap<>(2);
        keyMap.put("publicKey", new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded())));
        keyMap.put("privateKey", new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded())));
        return keyMap;
    }

    /**
     * 从文件中加载私钥
     *
     * @param path  文件所在路径
     * @param fileName 文件名称
     * @throws Exception
     */
    public static String loadKeyByFile(String path, String fileName) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + fileName));
            String readLine;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            throw new Exception("文件读取错误");
        } catch (NullPointerException e) {
            throw new Exception("输入流为空");
        }
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 需要被签名的数据（MD5值）
     * @param privateKey 私钥(BASE64编码)
     * @param signatureAlgorithm 指定签名算法
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey, String signatureAlgorithm) throws Exception {
        Signature signature = Signature.getInstance(signatureAlgorithm);
        signature.initSign(getPrivateKey(privateKey));
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data 需要被签名的数据（MD5值）
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * @param signatureAlgorithm 指定签名算法
     * @return
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign,
                                 String signatureAlgorithm) throws SignatureException {
        if(null == data ||data.length == 0
                || StringUtils.isEmpty(sign)
                || StringUtils.isEmpty(publicKey)
                || StringUtils.isEmpty(signatureAlgorithm)) {
           throw new SignatureException("签名验证参数为空");
        }
        try {
            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initVerify(RSAUtils.getPublicKey(publicKey));
            signature.update(data);
            // return signature.verify(Base64.getDecoder().decode(URLDecoder.decode(sign, "utf-8")));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            throw new SignatureException(String.format("签名失败，%s", e.getMessage()), e);
        }

    }
    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
        ByteArrayOutputStream out = getByteArrayData(cipher, encryptedData, false);
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey(publicKey));
        ByteArrayOutputStream out = getByteArrayData(cipher, encryptedData, false);
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));

        ByteArrayOutputStream out = getByteArrayData(cipher, data, true);

        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(privateKey));
        ByteArrayOutputStream out = getByteArrayData(cipher, data, true);
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * RSA加密明文最大长度117字节，解密要求密文最大长度为128字节，
     * 所以在加密和解密的过程中需要分块进行。
     * @param cipher 加密解密对象
     * @param data 加密后的数据或者需要加密的数据
     * @param isEncrypt 是否为加密，true 加密 false 解密
     * @return
     * @throws Exception
     */
    private static  ByteArrayOutputStream getByteArrayData(Cipher cipher,
                                                           byte[] data, boolean isEncrypt) throws Exception{
        byte[] cache;
        int i = 0;
        int inputLen = data.length;
        int offSet = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // RSA加密解密最大字节长度
        int maxDataBlock  = MAX_DECRYPT_BLOCK;
        if (isEncrypt) {
            maxDataBlock = MAX_ENCRYPT_BLOCK;
        }
        // 对数据分段处理
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxDataBlock) {
                cache = cipher.doFinal(data, offSet, maxDataBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxDataBlock;
        }
        return out;
    }

}
