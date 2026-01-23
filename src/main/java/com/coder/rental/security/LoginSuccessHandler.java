package com.coder.rental.security;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.coder.rental.entity.User;
import com.coder.rental.util.JWTUtil;
import com.coder.rental.util.RedisUtil;
import com.coder.rental.util.ResultCode;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 设置响应类型
        response.setContentType("application/json;charset=utf-8");
        // 从认证对象中获取登录的user对象
        User user =  (User) authentication.getPrincipal();

        // 生成token处理
        Map<String, Object> map = new HashMap<>(){{
            put("username", user.getUsername());
            put("userId", user.getId());
        }};
        // 生成token
        String token = JWTUtil.createToken(map);

        // 返回的就是时间戳，但是可能还会有一些时间格式的信息，所以会使用NumberWithFormat来接收
        NumberWithFormat claim = (NumberWithFormat) JWTUtil.parseToken(token).getClaim(JWTPayload.EXPIRES_AT);
        // 从中获取过期时间
        Long expireTime = Convert.toDate(claim).getTime();
        // 创建返回的对象
        AuthenticationResult authenticationResult = new AuthenticationResult(
                user.getId(),
                ResultCode.SUCCESS,
                token,
                expireTime
        );

        // 向客户端发生认证结果
        String result = JSON.toJSONString(authenticationResult,
                SerializerFeature.DisableCircularReferenceDetect);

        // 获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        // 将token存入redis中
        String tokenKey = "token:" + user.getId();
        Long nowTime = DateTime.now().getTime();
        redisUtil.set(tokenKey, token, (expireTime-nowTime)/1000);
    }
}
