package com.learn.springcloud.auth.jwt;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 如果请求没有检测到 token 则提示请求未授权。
 * @author shunzhong.deng
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {


    private static final long serialVersionUID = 3798723588865329956L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        resultMap.put("desc", "用户未授权访问");
        resultMap.put("exception", ExceptionUtils.getStackTrace(authException));

        PrintWriter printWriter = response.getWriter();
        printWriter.append(JSONObject.toJSONString(resultMap));
    }
}
