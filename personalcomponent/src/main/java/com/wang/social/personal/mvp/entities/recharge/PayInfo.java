package com.wang.social.personal.mvp.entities.recharge;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PayInfo {
    private String payInfo;

    @SerializedName("package")
    private String packageValue;
    private String appid;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String noncestr;
    private int timestamp;
}
