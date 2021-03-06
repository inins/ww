package com.wang.social.home.mvp.entities.funshow;

import com.frame.component.entities.funshow.FunshowBean;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FunshowHome implements Serializable{

    /**
     * userId : 10008
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
    private String province;
    private String city;
    private int mediaType;// 资源文件格式：1.音频2.视频3.图片
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
    //是否置顶
    private int isTop;
    //是否官方号
    private int isOfficial;

    public boolean isTop() {
        return isTop == 1;
    }

    public boolean isOfficial() {
        return isOfficial == 1;
    }

    public boolean isHideName() {
        return "1".equals(isAnonymous);
    }

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
        funshowBean.setHideName(isHideName());
        funshowBean.setTop(isTop());
        funshowBean.setOfficial(isOfficial());
        return funshowBean;
    }

    public static List<FunshowBean> tans2FunshowBeanList(List<FunshowHome> list) {
        List<FunshowBean> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (FunshowHome bean : list) {
                toList.add(bean.tans2FunshowBean());
            }
        }
        return toList;
    }
}
