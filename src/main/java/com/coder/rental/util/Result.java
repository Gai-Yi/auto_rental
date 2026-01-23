package com.coder.rental.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Result<T> {
    // 状态码
    private Integer code;
    // 返回的消息
    private String message;
    // 是否成功
    private Boolean success;
    // 返回的数据
    private T data;

    /*
     返回成功的消息，不带数据返回，新增、修改、删除操作使用
     */
    public static <T> Result<T> success() {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setSuccess(true)
                .setMessage("操作成功");
    }

    /*
    返回成功的消息，带返回值，查询操作使用
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setSuccess(true)
                .setMessage("操作成功")
                .setData(data);
    }

    /*
    返回失败的消息
     */
    public static <T> Result<T> fail() {
        return new Result<T>()
                .setCode(ResultCode.ERROR)
                .setSuccess(false)
                .setMessage("操作失败");
    }
}
