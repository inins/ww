package com.wang.social.funshow.mvp.entities.funshow;

import com.frame.component.entities.funshow.FunshowBean;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Funshow implements Serializable {


    /**
     * talkSupportNum : 2
     * city : null
     * talkCommentNum : 8
     * isSupport : 0
     * userName : 飘飘飘香
     * talkImage : ["http://1.jpg","http://2.gif"]
     * userId : 10000
     * talkImageNum : 2
     * content : 这是一个趣晒，很无趣
     * talkShareNum : null
     * province : null
     * isFree : 1
     * createTime : 1521787669000
     * price : 1000
     * talkId : 1
     * userCover : http://resouce.dongdongwedding.com/wangwang_2017-10-01_aae4ffd4-fa63-4984-8519-8ee0500d8258.jpg
     * isShopping : 1
     */

    private int talkSupportNum;
    private String city;
    private int talkCommentNum;
    private int isSupport;
    private String userName;
    private int userId;
    private int talkImageNum;
    private String content;
    private int talkShareNum;
    private String province;
    private int isFree;
    private long createTime;
    private int price;
    private int talkId;
    private String userCover;
    private int isShopping;
    private List<FunshowListRsc> talkImage;
    private FunshowVideoRsc resourceUrl;

    //////////////////////////////

    public boolean hasVideo() {
        return (resourceUrl != null && resourceUrl.isVidoe());
    }

    public FunshowListRsc getFirstImg() {
        if (StrUtil.isEmpty(talkImage)) return null;
        for (FunshowListRsc resource : talkImage) {
            if (resource.isImg()) {
                return resource;
            }
        }
        return null;
    }

    public FunshowBean tans2FunshowBean() {
        FunshowBean funshowBean = new FunshowBean();

        funshowBean.setId(talkId);
        funshowBean.setUserId(userId);
        funshowBean.setNickname(userName);
        funshowBean.setCreateTime(createTime);
        funshowBean.setContent(content);
        funshowBean.setAvatar(userCover);
        funshowBean.setShowPic(getFirstImg() != null ? getFirstImg().getUrl() : "");
        funshowBean.setPicNum(talkImageNum);
        funshowBean.setFree(isFree());
        funshowBean.setVideo(hasVideo());
        funshowBean.setSupportTotal(talkSupportNum);
        funshowBean.setCommentTotal(talkCommentNum);
        funshowBean.setShareTotal(talkShareNum);
        funshowBean.setSupport(isSupport());
        funshowBean.setCityName(city);
        funshowBean.setProvinceName(province);
        funshowBean.setPrice(price);
        funshowBean.setPay(!isShopping());

        if (hasVideo()) {
            funshowBean.setVideoUrl(resourceUrl.getUrl());
        }
        return funshowBean;
    }

    public static List<FunshowBean> tans2FunshowBeanList(List<Funshow> funshowList) {
        List<FunshowBean> funshowBeans = new ArrayList<>();
        if (!StrUtil.isEmpty(funshowList)) {
            for (Funshow funshow : funshowList) {
                funshowBeans.add(funshow.tans2FunshowBean());
            }
        }
        return funshowBeans;
    }

    ///////////////////////////////

    public boolean isSupport() {
        return isSupport != 0;
    }

    public void setIsSupport(boolean isSupport) {
        this.isSupport = isSupport ? 1 : 0;
    }

    public boolean isShopping() {
        return isShopping == 1;
    }

    public void setIsShopping(boolean isShopping) {
        this.isShopping = isShopping ? 1 : 0;
    }

    public boolean isFree() {
        return isFree == 1;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree ? 1 : 0;
    }
}
