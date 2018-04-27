package com.wang.social.funpoint.mvp.entities;

import com.wang.social.funpoint.utils.FunPointUtil;

import lombok.Data;

@Data
public class Funpoint {

    /**
     * newsId : 9
     * newsTitle : 标题
     * time : 1
     * src : 新闻来源
     * url : 新闻url
     * imgUrl : 新闻图片url
     * readTotal : 1000
     */

    private int newsId;
    private String newsTitle;
    private long time;
    private String src;
    private String url;
    private String imgUrl;
    private int readTotal;

    /////////////////////

    public String getNoteStr() {
        return FunPointUtil.getFunshowTimeStr(time) + " " + src + " " + readTotal + "阅读";
    }
}
