package com.frame.component.entities;

import android.text.TextUtils;

import com.frame.component.api.Api;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

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
    private int sex = -1;   //默认-1
    private long createTime;
    private String phone;
    private String birthday;
    private String province;
    private String city;
    private String constellation;
    private String autograph;
    private List<Tag> tags;

    /////////////////////////////////

    //是不是男性
    public boolean isMale() {
        return sex == 0;
    }

    public String getTagText() {
        if (StrUtil.isEmpty(tags)) return "";
        String tagText = "";
        for (Tag tag : tags) {
            tagText += "#" + tag.getTagName() + " ";
        }
        return tagText.trim();
    }

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

    public String getQrcodeImg() {
        return Api.DOMAIN + Api.USER_QRCODE + "?v=2.0.0&userId=" + userId;
    }
}
