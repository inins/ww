package com.frame.component.app;

import android.content.Context;

import com.frame.component.api.Api;
import com.frame.component.entities.HttpStatus;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.GlobalHttpHandler;
import com.frame.http.api.ApiException;
import com.frame.utils.FrameUtils;
import com.frame.utils.InterceptorUtils;

import java.util.LinkedHashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ========================================
 * {@link GlobalHttpHandler}实现类
 * <p>
 * Create by ChenJing on 2018-03-20 11:18
 * ========================================
 */

public class GlobalHttpHandlerImp implements GlobalHttpHandler {

    private Context mContext;

    public GlobalHttpHandlerImp(Context context) {
        this.mContext = context;
    }

    @Override
    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chan, Response response) {
        /*这里可以先客户端一步拿到Http的请求结果，可以做Json解析，错误码判断等*/
        MediaType mediaType = response.body().contentType();
        //如果返回的数据没有指定内容类型，或者是Json数据则进行错误码判断
        if (mediaType == null || mediaType.subtype() == null || mediaType.subtype().equalsIgnoreCase("json")) {
            HttpStatus httpStatus = FrameUtils.obtainAppComponentFromContext(mContext).gson().fromJson(httpResult, HttpStatus.class);
            if (httpStatus != null && httpStatus.getCode() != Api.SUCCESS_CODE) {
                throw new ApiException(httpStatus.getCode(), httpStatus.getMessage());
            }
        }
        return response;
    }

    @Override
    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
        /*如果需要再请求服务器之前做一些操作， 则重新返回一个做过操作的request（例如添加Header）*/

        Request.Builder requestBuilder = request.newBuilder();

        //FIXME:现在在接口配置中添加v 参数
//        //添加post公共参数(如果是post请求才添加)
//        InterceptorUtils.addFiled(request, requestBuilder, new LinkedHashMap<String, Object>() {{
//            put("v", "2.0.0");
//        }});
//        //添加url公共参数(如果是get请求才添加)
//        InterceptorUtils.addQuery(request, requestBuilder, new LinkedHashMap<String, Object>() {{
//            put("v", "2.0.0");
//        }});
        //添加公共请求头
        InterceptorUtils.addHeader(request, requestBuilder, new LinkedHashMap<String, Object>() {{
            put("Authorization", AppDataHelper.getToken());
        }});

        return requestBuilder.build();
    }
}