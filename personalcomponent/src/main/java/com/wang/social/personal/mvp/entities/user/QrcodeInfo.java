package com.wang.social.personal.mvp.entities.user;

import com.frame.component.api.Api;
import com.frame.component.entities.Tag;
import com.frame.utils.StrUtil;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class QrcodeInfo {

    /**
     * constellation : 白羊座
     * userTag : []
     * nickName : 大王的小妖怪
     * userAvatar : http://resouce.dongdongwedding.com/wangwang_2018-04-09_50a463e9-7ef7-412e-9be4-49c49182942b.jpg
     * userId : 10012
     * userAgeGroup : "用户年龄阶段"
     */

    private String constellation;
    @SerializedName("nickname")
    private String nickName;
    private String userAvatar;
    private int userId;
    private String userAgeGroup;
    private List<Tag> userTag;

    public String getQrcodeImg() {
        return Api.DOMAIN + Api.USER_QRCODE + "?v=2.0.0&userId=" + userId;
    }

    public String getTagText() {
        return Tag.getTagText(userTag);
    }
}
