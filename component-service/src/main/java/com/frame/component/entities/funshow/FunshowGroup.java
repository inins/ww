package com.frame.component.entities.funshow;

import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FunshowGroup {


    /**
     * address : null
     * supportTotal : 0
     * picNum : 2
     * commentTotal : 0
     * showPicMediaType : 2
     * readTotal : 4
     * avatar : http://resouce.dongdongwedding.com/FisxstkH-zOZjTyxO1Ej_8svMSBU
     * content : 哈哥给
     * relateState : 0
     * gemstone : null
     * cityName : null
     * createTime : 1526993310000
     * showPic : http://resouce.dongdongwedding.com/Data/wangwang/iOS/2018-05-22-ogsJjhnr.mp4
     * informUser : []
     * nickname : 小白
     * provinceName : null
     * talkId : 113
     */

    private String address;
    private int supportTotal;
    private int picNum;
    private int commentTotal;
    private int showPicMediaType;
    private int readTotal;
    private String avatar;
    private String content;
    private int relateState;
    private int gemstone;
    private String cityName;
    private long createTime;
    private String showPic;
    private String nickname;
    private String provinceName;
    private int talkId;
    private List<?> informUser;

    public boolean isFree() {
        return relateState == 0;
    }

//    public boolean isSupportBool() {
//        return isSupport == 1;
//    }

//    public void setIsSupportBool(boolean isSupportBool) {
//        isSupport = isSupportBool ? 1 : 0;
//    }

    public boolean hasVideo() {
        return showPicMediaType == 3;
    }

    public FunshowBean tans2FunshowBean() {
        FunshowBean funshowBean = new FunshowBean();

        funshowBean.setId(talkId);
//        funshowBean.setUserId(userId);
        funshowBean.setNickname(nickname);
        funshowBean.setCreateTime(createTime);
        funshowBean.setContent(content);
        funshowBean.setAvatar(avatar);
        funshowBean.setShowPic(showPic);
        funshowBean.setPicNum(picNum);
        funshowBean.setFree(isFree());
        funshowBean.setVideo(hasVideo());
        funshowBean.setSupportTotal(supportTotal);
        funshowBean.setCommentTotal(commentTotal);
//        funshowBean.setShareTotal(sha);
//        funshowBean.setSupport(isSupport());
        funshowBean.setCityName(cityName);
        funshowBean.setProvinceName(provinceName);
        funshowBean.setPrice(gemstone);
//        funshowBean.setPay(isShopping());
//        funshowBean.setHideName(isHideName());
        return funshowBean;
    }

    public static List<FunshowBean> tans2FunshowBeanList(List<FunshowGroup> list) {
        List<FunshowBean> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (FunshowGroup bean : list) {
                toList.add(bean.tans2FunshowBean());
            }
        }
        return toList;
    }
}
