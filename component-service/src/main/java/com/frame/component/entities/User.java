package com.frame.component.entities;

import android.text.TextUtils;

import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by liaoinstan on 2018/4/3.
 * 用户实体
 */
@Data
public class User implements Serializable {

    /**
     * id : 1
     * userId : 10000
     * nickname : 平东
     * avatar : http://resouce.dongdongwedding.com/Fuqg4sWTwi-D-7oqmqIkPa3ARBzu
     * sex : 1
     * createTime : 1505232000000
     * phone : 15528801837
     * birthday : 1990-12-31
     * constellation : 射手座
     */

    private int id;
    private int userId;
    private String nickname;
    private String avatar;
    private int sex;
    private long createTime;
    private String phone;
    private String birthday;
    private String province;
    private String city;
    private String constellation;
    private String autograph;

    /////////////////////////////////

    public String getSexText() {
        if (sex == 0) {
            return "男";
        } else if (sex == 1) {
            return "女";
        } else {
            return "";
        }
    }

    public int getProvinceInt() {
        return StrUtil.str2int(province);
    }

    public int getCityInt() {
        return StrUtil.str2int(city);
    }

//    public String getAstro() {
//        if (TextUtils.isEmpty(birthday)) return "";
//        if (birthday.indexOf(" ") != -1)
//            birthday = birthday.substring(birthday.indexOf(" "));
//        String[] split = birthday.split("-");
//        if (split == null || split.length < 3) return "";
//        int year = Integer.parseInt(split[0]);
//        int mouth = Integer.parseInt(split[1]);
//        int day = Integer.parseInt(split[2]);
//        return TimeUtils.getAstro(mouth, day);
//    }

    /////////////////////////////////

}
