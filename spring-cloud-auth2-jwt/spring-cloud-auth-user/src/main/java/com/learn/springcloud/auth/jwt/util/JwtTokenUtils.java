package com.learn.springcloud.auth.jwt.util;

import com.learn.springcloud.auth.jwt.exception.JwtTokenMalformedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * convenience class to generate a token for testing your requests.
 * Make sure the used secret here matches the on in your application.yml
 * @author shunzhong.deng
 */

@Component
public class JwtTokenUtils {
    private final Logger logger =  LoggerFactory.getLogger(getClass());

    public static final String CLAIM_KEY_AUTHORITIES = "scopes";

    /**
     * jwt对应的参数名称  JWT_TOKEN_HEAD=Authorization
     */
    private String TOKEN_HEAD = "Authorization";

    /**
     * token 的开始字符  JWT_HEADER=Bearer
     */
    private String HEADER = "Bearer";

    /**
     * token的过期时间  JWT_EXPIRATION=86400
     */
    private String JWT_EXPIRATION = "86400";

    /**
     * token签名的加密秘钥  JWT_SECRET=HFMJT
     */
    private String SECRET = "HFMJT";

    /**
     * 签发者
     */
    private String ISSUER = "user";

    /**
     * 面向的用户 JWT_SUBJECT=
     */
    private String SUBJECT ="SUBJECT";

    /**
     * 产生token
     * @param userId 用户唯一标志
     * @param scopes 用户的权限范围
     * @return
     */
    public  String generateToken(String userId, List<String> scopes) {

        DateTime dateTime = DateTime.now();
        Claims claims = Jwts.claims();

        // iss：Issuer，jwt 签发者
        claims.setIssuer(ISSUER);
        // sub：Subject，jwt 面向的用户
        claims.setSubject(SUBJECT);
        // aud：Audience，接收jwt的一方
        claims.setAudience(userId);

        // exp：Expiration time，jwt的过期时间，过期时间必须大于签发时间
        if (StringUtils.isEmpty(JWT_EXPIRATION) || !NumberUtils.isNumber(JWT_EXPIRATION)) {
            logger.warn("有效期参数JWT_EXPIRATION 未设置或设置错误，采用默认值86400（分钟）");
            JWT_EXPIRATION = "86400";
        }
        int expirationTime = Integer.valueOf(JWT_EXPIRATION);
        claims.setExpiration(dateTime.plusMinutes(expirationTime).toDate());

        // nbf：Not before，在什么之前，该jwt不可用。token 的生效时间
        claims.setNotBefore(dateTime.toDate());

        // iat：Issued at，jwt签发的时间
        claims.setIssuedAt(dateTime.toDate());
        // jti：JWT ID，jwt的唯一身份标识，避免重放攻击
        claims.setId(UUID.randomUUID().toString().replaceAll("-", ""));

        // 权限
        claims.put(CLAIM_KEY_AUTHORITIES, StringUtils.join(scopes.toArray(), ","));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 解析token 对象
     */
    public Claims parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return body;
        } catch (JwtException e) {
            throw  new JwtTokenMalformedException(String.format("token 解析错误_%s", e.getMessage()), e);
        }
    }



}
