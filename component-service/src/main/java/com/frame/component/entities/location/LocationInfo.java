package com.frame.component.entities.location;

import android.text.TextUtils;

import com.frame.utils.StrUtil;

import java.io.Serializable;

import lombok.Data;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-25 19:31
 * ============================================
 */
@Data
public class LocationInfo implements Serializable {
    /*
        纬度
     */
    private double latitude;
    /*
        经度
     */
    private double longitude;
    /*
        地点
     */
    private String place;
    /*
        地址
     */
    private String address;
    /*
        所在省份
     */
    private String province;
    /*
        所在城市
     */
    private String city;
    /*
        行政区划代码
     */
    private String adCode;

    public String getAddress() {
        if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)) {
            return StrUtil.subFirstChart(address, province + city);
        } else {
            return address;
        }
    }
}
