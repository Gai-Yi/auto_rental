package com.coder.rental.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import com.coder.rental.security.CustomerAuthenticationException;

import java.util.Map;

public class JWTUtil {
    // JWT密钥
    public static final String SECRET_KEY = "gaiyi";
    // 过期时间，30分钟
    public static final long EXPIRE_TIME = 1000L * 60 * 30;

    /*
    创建token
     */
    public static String createToken(Map<String, Object> payload) {
        DateTime now = DateTime.now();
        DateTime expireTime = new DateTime(now.getTime() + EXPIRE_TIME);
        // 设置Token的签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 设置Token的过期时间
        payload.put(JWTPayload.EXPIRES_AT, expireTime);
        // 设置Token的生效时间，确保Token在签发后立即生效
        payload.put(JWTPayload.NOT_BEFORE, now);
        return cn.hutool.jwt.JWTUtil.createToken(payload, SECRET_KEY.getBytes());
    }

    /*
    解析token
     */
    public static JWTPayload parseToken(String token) {
        JWT jwt = cn.hutool.jwt.JWTUtil.parseToken(token);
        // 这里是setKey，原因是密钥既可以用来加密也可以用来解密
        if (!jwt.setKey(SECRET_KEY.getBytes()).verify()) {
            // 因为不是AuthenticationException的子类，所以不会被捕获到
            // 最终交给SpringSecurity处理为匿名访问的异常
            // throw new RuntimeException("token异常");
            throw new CustomerAuthenticationException("token异常");
        }
        if (!jwt.validate(0)) {
            throw new CustomerAuthenticationException("token已过期");
        }
        return jwt.getPayload();
    }

    public static void main(String[] args) {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("username", "admin");
//        payload.put("password", "123456");
//        String token = createToken(payload);
//        System.out.println(token);

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsIm5iZiI6MTc2NzQ0MTE5NywiZXhwIjoxNzY3NDQyOTk3LCJpYXQiOjE3Njc0NDExOTcsInVzZXJuYW1lIjoiYWRtaW4ifQ.K73ujOpaOOP1kl5_r9YWffBNrRxBeq5FwoaHaRLxi7U";
        JWTPayload payload = parseToken(token);
        System.out.println(payload.getClaim("username"));
        System.out.println(payload.getClaim("password"));
        NumberWithFormat claim = (NumberWithFormat) payload.getClaim(JWTPayload.EXPIRES_AT);
        DateTime convert = (DateTime) claim.convert(DateTime.class, claim);
        long expireTime = convert.getTime();
        long nowTime = DateTime.now().getTime();
        System.out.println((expireTime-nowTime) / 1000);
    }
}
