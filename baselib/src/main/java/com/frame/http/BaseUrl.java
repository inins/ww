package com.frame.http;

import okhttp3.HttpUrl;

/**
 * =========================================
 * 针对BaseUrl在App启动时不能确定，需要请求服务器动态获取的应用场景
 * <p>
 * Create by ChenJing on 2018-03-16 13:14
 * =========================================
 */

public interface BaseUrl {

    /**
     * 在调用 Retrofit Api 接口之前，使用 OkHttp 或其他方式获取 BaseUrl，并通过此方法返回
     * @return
     */
    HttpUrl url();
}
