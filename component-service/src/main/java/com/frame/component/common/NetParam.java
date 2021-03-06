package com.frame.component.common;

import android.os.Build;
import android.util.Log;

import com.frame.component.entities.location.LocationInfo;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.utils.ChannelUtils;
import com.frame.component.utils.MapUtil;
import com.frame.component.utils.SignUtil;
import com.frame.component.utils.UrlUtil;
import com.frame.utils.AppUtils;
import com.frame.utils.PhoneUtils;
import com.frame.utils.StrUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by liaointan on 2017/7/17.
 * 网络请求参数封装，便于统一管理，拓展后期可能增加的统一参数，加密设置等处理过程
 */

public class NetParam {
    private Map<String, Object> paramMap = new LinkedHashMap<>();

    public static NetParam newInstance() {
        return new NetParam();
    }

    public NetParam() {
    }

    //添加参数
    public NetParam put(String key, Object value) {
        //如果参数值为null则不放进参数map
        if (value == null)
            return this;
        paramMap.put(key, value);
        return this;
    }

    //添加签名参数
    //会对所有参数进行加密校验，必须在参数全部加入后调用该方法
    public NetParam putSignature() {
        if (StrUtil.isEmpty(paramMap)) return this;
        String randomInt = String.valueOf(new Random().nextInt());
        paramMap.put("nonceStr", randomInt);
        paramMap.put("signature", SignUtil.signStr(MapUtil.transLinkedHashMap(paramMap), randomInt));
        return this;
    }

    //添加通用统计参数
    public NetParam putStaticParam() {
        //版本号
        int versionCode = AppUtils.getAppVersionCode();
        //渠道号
        int channelCode = ChannelUtils.getChannelCode();
        //获取经纬度，没有则为null
        LocationInfo locationInfo = AppDataHelper.getLocationInfo();
        Double longitude = null;
        Double latitude = null;
        if (locationInfo != null) {
            longitude = locationInfo.getLongitude();
            latitude = locationInfo.getLatitude();
        }
        paramMap.put("devicesKey", PhoneUtils.getIMEI());
        paramMap.put("channelId", channelCode + "");
        paramMap.put("appVersion", versionCode + "");
        paramMap.put("devicesModel", Build.MODEL);
        paramMap.put("devicesSystem", "android" + Build.VERSION.RELEASE);
        if (longitude != null) paramMap.put("longitude", longitude + "");
        if (latitude != null) paramMap.put("latitude", latitude + "");
        return this;
    }

    public NetParam put(Map<String, Object> map) {
        paramMap.putAll(map);
        return this;
    }

    //构建参数集合
    public Map<String, Object> build() {
        return paramMap;
    }

    //################  工具类方法  #################

    //构造一个上传文件的Bodypart
    public static MultipartBody.Part buildFileBodyPart(String partName, String path) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        return body;
    }

    //构造一个RequestBody
    public static RequestBody buildJsonRequestBody(Object o) {
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(o));
    }

    //测试方法，打印出参数
    private void pritParam() {
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            Log.e("NetParam", entry.getKey() + ":" + entry.getValue().toString());
        }
    }

    public static String createUrl(String url, Map<String, Object> map) {
        return UrlUtil.addParams(url, map);
    }
}
