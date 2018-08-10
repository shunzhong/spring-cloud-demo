package com.learn.springcloud.auth.jwt;


import com.learn.springcloud.auth.jwt.exception.JwtTokenMissingException;
import com.learn.springcloud.auth.jwt.model.JwtAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JWT token 过滤器
 * @author shunzhong.deng
 */
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {


    @Value("${JWT_HEADER}")
    private String tokenHeader;

    @Value("${JWT_TOKEN_HEAD}")
    private String tokenHead;

    @Value("${JWT_PASS_URL}")
    private String JWT_PASS_URL;

    @Value("${JWT_PASS_URL_SPLIT}")
    private String JWT_PASS_URL_SPLIT;

    @Value("${JWT_PASS_STATIC_RESOURCE}")
    private String JWT_PASS_STATIC_RESOURCE;


    /**
     * 定义过滤器拦截所有请求
     */
    public JwtAuthenticationTokenFilter() {
        super("/**");
    }


    /**
     * 封装token请求参数，并传递给授权管理器
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        // 获取token数据
        String header = request.getHeader(this.tokenHead);

        if (header == null || !header.startsWith(tokenHeader)) {
            throw new JwtTokenMissingException("token 不存在或格式错误");
        }

        String authToken = header.substring(tokenHeader.length());

        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);

        return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 授权成功处理逻辑
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param chain      过滤器链
     * @param authResult 授权结果
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }

    /**
     * 重写当前过滤器处理逻辑，开放无需授权的服务
     * 用户注册     /auth/register
     * 用户登录     /auth/login
     * 首页         /home/**
     * 在线评估     /assessment/**
     * 等服务不拦截
     *
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String token = request.getHeader(this.tokenHead);
        // 所有需要放行的请求地址
        List<String> passUrls = new ArrayList<>(10);

        // 设置默认分割符
        if (StringUtils.isEmpty(JWT_PASS_URL_SPLIT)){
            JWT_PASS_URL_SPLIT = ",";
        }
        // 拒绝放行所有拦截，防止配置错误
        if (!StringUtils.isEmpty(JWT_PASS_STATIC_RESOURCE) && !"/**".equals(JWT_PASS_STATIC_RESOURCE)) {
            // 放行静态资源
            if (JWT_PASS_STATIC_RESOURCE.contains(JWT_PASS_URL_SPLIT)) {
                passUrls.addAll(Arrays.asList(JWT_PASS_STATIC_RESOURCE.split(JWT_PASS_URL_SPLIT)));
            } else {
                passUrls.add(JWT_PASS_STATIC_RESOURCE);
            }
        }

        // 请求中包含token 表示需要授权
        if (StringUtils.isEmpty(token) &&  !"/**".equals(JWT_PASS_URL) && !StringUtils.isEmpty(JWT_PASS_URL)) {
            if (JWT_PASS_URL.contains(JWT_PASS_URL_SPLIT)) {
                // 放行匿名访问资源
                passUrls.addAll(Arrays.asList(JWT_PASS_URL.split(JWT_PASS_URL_SPLIT)));
            } else {
                passUrls.add(JWT_PASS_URL);
            }
        }

        boolean isPass = false;
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String passUrl : passUrls) {
            if (pathMatcher.match(passUrl, getRequestPath(request))){
                isPass = true;
                break;
            }
        }

        if (isPass) {
            chain.doFilter(req,res);
            return;
        }

        // 不是登录、注册、首页不用
        super.doFilter(req, res, chain);

    }

    /**
     * 通过request 获取url
     *
     * @param request
     * @return
     */

    private String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();

        if (request.getPathInfo() != null) {
            url += request.getPathInfo();
        }

        return url;
    }

}