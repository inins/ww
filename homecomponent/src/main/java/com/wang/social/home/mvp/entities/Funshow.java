package com.wang.social.home.mvp.entities;

import lombok.Data;

@Data
public class Funshow {

    /**
     * creatorId : 10008
     * createTime : 1522068104000
     * nickname : 洗白白
     * headImg : http://resouce.dongdongwedding.com/wan7405.jpg
     * talkId : 496
     * content : 草泥马
     * address : 天府大道北段思拓国际托福雅思SAT培训中心
     * mediaType : 3
     * readTotal : 0
     * commentTotal : 0
     * supportTotal : 0
     * shareTotal : 0
     * relateState : 0
     * relateMoney : 0
     * talkLiked : 0
     * talkPayed : 0
     * isAnonymous : 0
     * url : http://resouce.dongdongwedding.com/wangwance9ca.jpg
     * urls : http://resouce.dongdonggw20140.jpg
     */

    private int creatorId;
    private long createTime;
    private String nickname;
    private String headImg;
    private int talkId;
    private String content;
    private String address;
    private int mediaType;
    private int readTotal;
    private int commentTotal;
    private int supportTotal;
    private int shareTotal;
    private int relateState;
    private int relateMoney;
    private int talkLiked;
    private int talkPayed;
    private String isAnonymous;
    private String url;
    private int urls;

    public boolean isFree() {
        return relateState == 0;
    }

    public boolean isLiked() {
        return talkLiked == 1;
    }

    public boolean isVideo() {
        return mediaType == 2;
    }
}
