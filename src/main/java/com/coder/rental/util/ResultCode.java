package com.coder.rental.util;

/**
 * 定义HTTP协议状态码
 */
public class ResultCode {
    // 表示请求成功的状态码
    public static final Integer SUCCESS = 200;
    // 表示服务器内部错误的状态码
    public static final Integer ERROR = 500;
    // 表示请求需要用户验证的状态码，授权过期时使用
    public static final Integer UNAUTHENTICATED = 401;
    // 表示请求未授权的状态码，授权错误，未授权等情况使用
    public static final Integer UNAUTHORIZED = 403;
}
