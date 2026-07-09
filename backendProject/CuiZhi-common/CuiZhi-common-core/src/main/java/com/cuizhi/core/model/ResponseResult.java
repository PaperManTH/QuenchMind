package com.cuizhi.core.model;

import com.cuizhi.core.common.CzHttpStatus;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @Author thpaperman
 * @Description 统一返回结果
 * @Date 2026/4/6
 * @Version 1.0
 */
@Data
@ToString
public class ResponseResult<T> {


    /**
     * 错误码
     */
    private int code;

    /**
     * 错误消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public ResponseResult() {
        this(HttpStatus.OK.value(), "success");
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>();
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(CzHttpStatus.SUCCESS.getCode(), CzHttpStatus.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseResult<T> success(T data, String msg) {
        return new ResponseResult<T>(CzHttpStatus.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResponseResult<T> error(int code, String msg) {
        return new ResponseResult<T>(code, msg);
    }

    public static <T> ResponseResult<T> error(int code, String msg, T data) {
        return new ResponseResult<T>(code, msg, data);
    }

    public static <T> ResponseResult<T> error(String msg) {
        return new ResponseResult<T>(CzHttpStatus.INTERNAL_ERROR.getCode(), msg);
    }
}
