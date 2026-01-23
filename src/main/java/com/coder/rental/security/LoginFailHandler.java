package com.coder.rental.security;

import com.alibaba.fastjson.JSON;
import com.coder.rental.util.Result;
import com.coder.rental.util.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*
登录失败的处理器，经过登录过滤器时，处理失败的情况下会调用这个处理器
 */
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();

        // 初始化错误状态码和错误消息
        int code = ResultCode.ERROR;
        String msg = null;

        // instanceof判断对象是否为子类
        // 根据处理失败的异常来返回对应的消息
        if (exception instanceof AccountExpiredException) {
            code = ResultCode.UNAUTHENTICATED;
            msg = "账户已过期";
        } else if (exception instanceof BadCredentialsException) {
            code = ResultCode.UNAUTHORIZED;
            msg = "账户或密码错误";
        } else if (exception instanceof CredentialsExpiredException) {
            code = ResultCode.UNAUTHORIZED;
            msg = "密码已过期";
        } else if (exception instanceof DisabledException) {
            code = ResultCode.UNAUTHORIZED;
            msg = "账户已锁定";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            code = ResultCode.UNAUTHORIZED;
            msg = "账户不存在";
        } else if (exception instanceof CustomerAuthenticationException) {
            code = ResultCode.UNAUTHORIZED;
            msg = exception.getMessage();
        } else {
            msg = "登录失败";
        }

        // 将失败的消息封装到result对象，转化为json后写到response响应体中去
        String result = JSON.toJSONString(Result.fail().setMessage(msg).setCode(code));
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
