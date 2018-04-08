package com.frame.component.utils;

import com.frame.utils.StrUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liaoinstan on 2017/8/11.
 */

public class MapUtil {

    //把一个Map集合转换为HashMap
    public static <K, T> HashMap<K, T> transHashMap(Map<K, T> fromMap) {
        if (fromMap instanceof HashMap) {
            return (HashMap<K, T>) fromMap;
        } else {
            HashMap<K, T> toMap = new HashMap<>();
            for (Map.Entry<K, T> entry : fromMap.entrySet()) {
                toMap.put(entry.getKey(), entry.getValue());
            }
            return toMap;
        }
    }

    //把一个Map集合转换为HashMap
    public static LinkedHashMap<String, String> transLinkedHashMap(Map<String, Object> fromMap) {
        LinkedHashMap<String, String> toMap = new LinkedHashMap<>();
        if (!StrUtil.isEmpty(fromMap)) {
            for (Map.Entry<String, Object> entry : fromMap.entrySet()) {
                toMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return toMap;
    }
}
