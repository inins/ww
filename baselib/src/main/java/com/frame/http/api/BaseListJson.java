package com.frame.http.api;

import java.util.List;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:30
 * ========================================
 */

public class BaseListJson<T> {

    private int code;
    private String message;
    private List<T> data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }
}
