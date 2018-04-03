package com.frame.utils;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/4/3.
 */

public class InterceptorUtils {

    //添加post公共参数
    public static void addFiled(Request request, Request.Builder requestBuilder, Map<String, Object> paramMap) {
        if (StrUtil.isEmpty(paramMap) || request == null || requestBuilder == null)
            return;
        if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                //把原来的参数添加到新的构造器
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
                //添加新参数
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    bodyBuilder.addEncoded(entry.getKey(), String.valueOf(entry.getValue()));
                }
                requestBuilder.post(bodyBuilder.build());
            }
        }
    }

    //添加url公共参数
    public static void addQuery(Request request, Request.Builder requestBuilder, Map<String, Object> paramMap) {
        if (StrUtil.isEmpty(paramMap) || request == null || requestBuilder == null)
            return;
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
        }
        requestBuilder.url(urlBuilder.build());
    }
    //添加公共请求头
    public static void addHeader(Request request, Request.Builder requestBuilder, Map<String, Object> paramMap) {
        if (StrUtil.isEmpty(paramMap) || request == null || requestBuilder == null)
            return;
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            requestBuilder.header(entry.getKey(), String.valueOf(entry.getValue()));
        }
    }
}
