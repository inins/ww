package com.wang.social.funshow.mvp.entities.user;

import lombok.Data;

@Data
public class ZanUser {

    /**
     * objectId : 1
     * objectType : 1
     * userId : 10013
     * nickname : W-NewType
     * avatar : http://resouce.dongdongwedding.com/FisxstkH-zOZjTyxO1Ej_8svMSBU
     * tags : 时尚,电影,电视剧,直播,综艺
     * sex : 1
     * birthday : 918489600000
     * constellation : 摩羯座
     */

    private int objectId;
    private String objectType;
    private int userId;
    private String nickname;
    private String avatar;
    private String tags;
    private int sex;
    private long birthday;
    private String constellation;

    //////////////////////////////////////
    //是不是男性
    public boolean isMale() {
        return sex == 0;
    }
}
