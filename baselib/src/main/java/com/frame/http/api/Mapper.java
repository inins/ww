package com.frame.http.api;

/**
 * =========================================
 * 框架要求所有DTO类实现此接口，以满足规范
 * <p>
 * Create by ChenJing on 2018-03-20 15:33
 * =========================================
 */

public interface Mapper<T> {

    T transform();
}
