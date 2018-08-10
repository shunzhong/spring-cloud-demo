package com.learn.springcloud.auth.jwt;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt 授权失败
 * @author shunzhong.deng
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=utf-8");

        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        resultMap.put("desc", "用户未授权访问");
        resultMap.put("exception", "Token验证失败_" +ExceptionUtils.getStackTrace(exception));
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JSONObject.toJSONString(resultMap));
    }
}
