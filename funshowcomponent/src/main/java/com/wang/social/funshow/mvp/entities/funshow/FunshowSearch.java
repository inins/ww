package com.wang.social.funshow.mvp.entities.funshow;

import com.frame.component.entities.funshow.FunshowBean;
import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FunshowSearch {


    /**
     * creatorId : 10013
     * createTime : 1524817294000
     * nickname : 小白
     * headImg : http://resouce.dongdongwedding.com/FisxstkH-zOZjTyxO1Ej_8svMSBU
     * talkId : 49
     * content : 测试收费11111111111
     * province : 四川省
     * city : 成都市
     * address : 天府大道北段1700号新世纪环球中心E1-1012号
     * mediaType : 1
     * readTotal : 3
     * commentTotal : 0
     * supportTotal : 0
     * shareTotal : 0
     * relateState : 1
     * gemstone : 500
     * talkLiked : 0
     * talkPayed : 0
     * isAnonymous : 0
     * url : http://resouce.dongdongwedding.com/Data/wangwang/iOS-2018-04-27-Ye4iRALr.png
     * urls : 18
     */

    private int creatorId;
    private long createTime;
    private String nickname;
    private String headImg;
    private int talkId;
    private String content;
    private String province;
    private String city;
    private String address;
    private int mediaType;
    private int readTotal;
    private int commentTotal;
    private int supportTotal;
    private int shareTotal;
    private int relateState;
    private int gemstone;
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

    public void setIsLike(boolean isLike) {
        talkLiked = isLike ? 1 : 0;
    }

    public boolean isVideo() {
        return mediaType == 2;
    }

    public boolean isPay() {
        return talkPayed == 1;
    }

    public FunshowBean tans2FunshowBean() {
        FunshowBean funshowBean = new FunshowBean();

        funshowBean.setId(talkId);
        funshowBean.setUserId(creatorId);
        funshowBean.setAvatar(headImg);
        funshowBean.setNickname(nickname);
        funshowBean.setCreateTime(createTime);
        funshowBean.setContent(content);
        funshowBean.setShowPic(url);
        funshowBean.setPicNum(urls);
        funshowBean.setFree(isFree());
        funshowBean.setVideo(isVideo());
        funshowBean.setSupportTotal(supportTotal);
        funshowBean.setCommentTotal(commentTotal);
        funshowBean.setShareTotal(shareTotal);
        funshowBean.setSupport(isLiked());
        funshowBean.setCityName(city);
        funshowBean.setProvinceName(province);
        funshowBean.setPrice(gemstone);
        funshowBean.setPay(isPay());
        return funshowBean;
    }

    public static List<FunshowBean> tans2FunshowBeanList(List<FunshowSearch> list) {
        List<FunshowBean> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (FunshowSearch bean : list) {
                toList.add(bean.tans2FunshowBean());
            }
        }
        return toList;
    }
}
