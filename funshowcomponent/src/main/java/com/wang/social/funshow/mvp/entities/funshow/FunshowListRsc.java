package com.wang.social.funshow.mvp.entities.funshow;

import com.frame.utils.StrUtil;

import lombok.Data;

@Data
public class FunshowListRsc {

    /**
     * id : 1
     * talkId : 1
     * mediaType : 3
     * url : http://resouce.dongdongwedding.com/wangwang_2017-06-16_6d7da9a7-e8dd-4552-b8eb-5c49e4df8665.jpg
     * musicId : null
     * picType : null
     * userId : 1000
     * picOrder : 1
     * createTime : 1521773924000
     * extendData : null
     */

    private int id;
    private int talkId;
    private int mediaType;
    private String url;
    private int musicId;
    private int picType;
    private int userId;
    private int picOrder;
    private long createTime;
    private Object extendData;

    ///////////////////////////

    public boolean isImg() {
        return mediaType == 3;
    }

    public boolean isVideo() {
        return mediaType == 2;
    }

    public boolean isVoice() {
        return mediaType == 1;
    }
}
