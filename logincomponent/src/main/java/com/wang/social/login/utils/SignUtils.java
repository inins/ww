package com.wang.social.login.utils;

import com.frame.component.utils.MapUtil;
import com.frame.utils.PayUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class SignUtils {

    public static String getSign(String mobile, String password) {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("mobile", mobile);
        paramMap.put("password", password);

        return getSign(paramMap);
    }

    public static String getSign(Map<String, Object> paramMap) {
        paramMap.put("v","2.0.0");
        String randomInt = String.valueOf(new Random().nextInt());
        paramMap.put("nonceStr",randomInt);
        return PayUtil.signStr(MapUtil.transLinkedHashMap(paramMap), randomInt);
    }
}
