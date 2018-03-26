package com.frame.http.api;

/**
 * ========================================
 * 接口请求错误异常
 * <p>
 * Create by ChenJing on 2018-03-20 14:23
 * ========================================
 */

public class ApiException extends RuntimeException{

    private int errorCode;
    private String msg;

    public ApiException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}
