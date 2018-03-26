package com.frame.http.api;

/**
 * ========================================
 * 接口返回Json数据对应实体
 * <p>
 * Create by ChenJing on 2018-03-20 14:25
 * ========================================
 */

public class BaseJson<T> {

    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
