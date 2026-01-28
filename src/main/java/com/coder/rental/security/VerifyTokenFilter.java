package com.coder.rental.security;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import com.coder.rental.util.JWTUtil;
import com.coder.rental.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
OncePerRequestFilter是SpringBoot提供的过滤每一个请求的抽象类
继承这个抽象类后，我们的过滤器可以过滤每一个请求
 */
@Component
public class VerifyTokenFilter extends OncePerRequestFilter {
    @Value("${request.loginUrl}")
    private String loginUrl;

    @Value("${request.imageUrl}")
    private String imageUrl;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private LoginFailHandler loginFailHandler;

    @Resource
    private CustomerUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 当请求的地址不是登录的地址的时候进行认证，如果是的话则进行后面的过滤链
        if (!StrUtil.equals(requestURI, loginUrl) && !StrUtil.equals(requestURI, imageUrl)) {
            try {
                // 校验token
                verifyToken(request, response);
            } catch (AuthenticationException e) {
                // 我们在校验token的时候抛出了很多AuthenticationException的子异常
                // 当抛出异常了以后，这里会捕获到异常然后进入到失败的处理器进行处理
                loginFailHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 放行
        doFilter(request, response, filterChain);
    }

    public void verifyToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StrUtil.isEmpty(token)) {
            throw new CustomerAuthenticationException("token为空");
        }
        // 从token中解析并拼接出来一个tokenKey，然后在redis中进行匹配拿到对应的值
        JWTPayload payload = JWTUtil.parseToken(token);
        NumberWithFormat numberWithFormat = (NumberWithFormat) payload.getClaim("userId");
        Integer userId = numberWithFormat.intValue();
        System.out.println(userId + "======================");
        String tokenKey = "token:" + userId;
        String tokenValue = redisUtil.get(tokenKey);

        // 如果查不出来值则说明token已经过期
        if (StrUtil.isEmpty(tokenValue)) {
            throw new CustomerAuthenticationException("token已过期");
        }
        System.out.println("tokenValue:" + tokenValue);
        System.out.println("token:" + token);
        // 如果从请求中获取的token，和根据请求中解析出来的id拼接出来的key然后在redis中查询的token不一样
        // 说明token的内容被改过了，返回一个token异常的错误
        if (!tokenValue.equals(token)) {
            throw new CustomerAuthenticationException("token异常");
        }
        String username = (String) payload.getClaim("username");
        // 如果解析出来的用户名是空的，也就是说没有用户名参数，说明token有问题
        if (StrUtil.isEmpty(username)) {
            throw new CustomerAuthenticationException("token解析错误");
        }

        // 获取UserDetails对象
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 创建通行的令牌，参数包括用户的详细信息、用户凭证（密码等）这里token已经校验过了，以及最后的用户权限信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

        // 添加请求的参数信息，包括请求的IP和会话等，可以用来校验是否在黑名单等等状态
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 将认证信息加入到Security上下文环境中
        // 这样的话后面的过滤链就可以知道这个请求已经获取了认证令牌，就不会再进行其它验证了
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
