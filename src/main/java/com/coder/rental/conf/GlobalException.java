package com.coder.rental.conf;

import com.coder.rental.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 全局异常处理类
@Slf4j // 记录日志
public class GlobalException {

    // 处理所有异常
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e) {
        log.error("异常信息：{}", e.getMessage());
        e.printStackTrace();
        return Result.fail().setMessage(e.getMessage());
    }
}
