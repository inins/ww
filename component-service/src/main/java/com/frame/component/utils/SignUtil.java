package com.frame.component.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SignUtil {

    private static final String JAVA_APP = "javaapp";
    /**
     * 外部调用自己系统接口加密规则
     * 1.对参数进行a-z排序(ASCII码从小到大排序（字典序))
     * 2.语言(javaapp)进行md5加密，并且拼接两端
     * 3.取出随机数，并且拼接到两端
     * 4.字符串进行md5加密
     *
     * @return
     */
    public static String signStr(Map<String, String> params, String creditCode) {
        try {
            List<Map.Entry<String, String>> sortMap = sortMapByKey(params);
            // 获取系统加密字符串
            String typeMd5 = md5(JAVA_APP);
            StringBuffer bf = new StringBuffer(typeMd5);
            bf.append("&");
            bf.append(creditCode);
            bf.append("&");
            for (Map.Entry<String, String> param : sortMap) {
                bf.append(param.getKey()).append("=").append(param.getValue())
                        .append("&");
            }
            bf.append(creditCode);
            bf.append("&");
            bf.append(typeMd5);

            Log.e("test",bf.toString());
            // 加密后字符串
            return md5(bf.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 排序Map
     *
     * @param oriMap
     * @return
     */
    private static List<Map.Entry<String, String>> sortMapByKey(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        List<Map.Entry<String, String>> list = new ArrayList<>(oriMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {

                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return list;
    }


}
