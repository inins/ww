package com.frame.component.entities.funshow;

import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FunshowMe {

    /**
     * birthday : 730310400000
     * address : 武侯区桂溪街道天府大道中段辅路1062号成都环球中心天堂洲际大饭店
     * supportTotal : 0
     * cityCode : null
     * provinceCode : null
     * isSupport : 0
     * userId : 10012
     * showOrHide : null
     * picNum : 1
     * commentTotal : 2
     * share_total : 0
     * showPicMediaType : 2
     * readTotal : 1
     * avatar : http://resouce.dongdongwedding.com/wangwang_2018-04-17_a697c09b-d9a9-4535-9624-cb9420d5a3a3.jpg
     * content : 视频3@飘飘飘香收费测试
     * relateState : 1
     * gemstone : 10
     * cityName : null
     * createTime : 1525351964000
     * showPic : http://resouce.dongdongwedding.com/wangwang_2018-05-03_dcc34bca-d482-4ad0-85d1-f876216b4af1.mp4
     * informUser : [{"nickname":null,"userId":10000}]
     * nickname : _JJ
     * id : 61
     */

    private long birthday;
    private String address;
    private int supportTotal;
    private int cityCode;
    private int provinceCode;
    private int isSupport;
    private int creatorId;
    private int showOrHide;
    private int picNum;
    private int commentTotal;
    private int share_total;
    private int showPicMediaType;
    private int readTotal;
    private String avatar;
    private String content;
    private int relateState;
    private int gemstone;
    private String cityName;
    private long createTime;
    private String provinceName;
    private String showPic;
    private String nickname;
    private int id;

    public boolean isFree() {
        return relateState == 0;
    }

    public boolean isSupportBool() {
        return isSupport == 1;
    }

    public void setIsSupportBool(boolean isSupportBool) {
        isSupport = isSupportBool ? 1 : 0;
    }

    public boolean hasVideo() {
        return showPicMediaType == 3;
    }

    public FunshowBean tans2FunshowBean() {
        FunshowBean funshowBean = new FunshowBean();

        funshowBean.setId(id);
        funshowBean.setUserId(creatorId);
        funshowBean.setNickname(nickname);
        funshowBean.setCreateTime(createTime);
        funshowBean.setContent(content);
        funshowBean.setShowPic(showPic);
        funshowBean.setPicNum(picNum);
        funshowBean.setFree(isFree());
        funshowBean.setVideo(hasVideo());
        funshowBean.setSupportTotal(supportTotal);
        funshowBean.setCommentTotal(commentTotal);
        funshowBean.setShareTotal(share_total);
        funshowBean.setSupport(isSupportBool());
        funshowBean.setCityName(cityName);
        funshowBean.setProvinceName(provinceName);
        funshowBean.setPrice(gemstone);
        funshowBean.setPay(true);   //自己的趣晒不需要支付，默认为已经支付
        return funshowBean;
    }

    public static List<FunshowBean> tans2FunshowBeanList(List<FunshowMe> funshowMeList) {
        List<FunshowBean> funshowBeans = new ArrayList<>();
        if (!StrUtil.isEmpty(funshowMeList)) {
            for (FunshowMe funshowMe : funshowMeList) {
                funshowBeans.add(funshowMe.tans2FunshowBean());
            }
        }
        return funshowBeans;
    }
}
