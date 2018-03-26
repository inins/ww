package com.frame.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ========================================
 * http 请求和响应结果得处理类
 * <p>
 * <p>
 * Create by ChenJing on 2018-03-16 11:02
 * ========================================
 */

public interface GlobalHttpHandler {

    Response onHttpResultResponse(String httpResult, Interceptor.Chain chan, Response response);

    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);

    //空实现
    GlobalHttpHandler EMPTY = new GlobalHttpHandler() {
        @Override
        public Response onHttpResultResponse(String httpResult, Interceptor.Chain chan, Response response) {
            return response;
        }

        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            return request;
        }
    };
}
