package com.frame.component.entities.dto;

import com.frame.component.entities.funshow.FunshowBean;
import com.frame.http.api.Mapper;
import com.frame.component.utils.EntitiesUtil;

import lombok.Data;

@Data
public class TalkBeanDTO implements Mapper<FunshowBean> {
    private Integer creatorId;
    private Long createTime;
    private String nickname;
    private String headImg;
    private Integer talkId;
    private String content;
    private String address;
    private Integer mediaType;
    private Integer readTotal;
    private Integer commentTotal;
    private Integer supportTotal;
    private Integer shareTotal;
    private Integer relateState;
    private Integer gemstone;
    private Integer talkLiked;
    private Integer talkPayed;
    private String isAnonymous;
    private Integer urls;
    private String url;
    private String province;
    private String city;

    @Override
    public FunshowBean transform() {
        FunshowBean object = new FunshowBean();

        object.setId(EntitiesUtil.assertNotNull(talkId));
        object.setUserId(EntitiesUtil.assertNotNull(creatorId));
        object.setCreateTime(EntitiesUtil.assertNotNull(createTime));
        object.setNickname(EntitiesUtil.assertNotNull(nickname));
        object.setAvatar(EntitiesUtil.assertNotNull(headImg));
        object.setContent(EntitiesUtil.assertNotNull(content));
        object.setShareTotal(EntitiesUtil.assertNotNull(readTotal));
        object.setCommentTotal(EntitiesUtil.assertNotNull(commentTotal));
        object.setSupportTotal(EntitiesUtil.assertNotNull(supportTotal));
        object.setShareTotal(EntitiesUtil.assertNotNull(shareTotal));
        object.setFree(relateState == 0);
        object.setPrice(EntitiesUtil.assertNotNull(gemstone));
        object.setShowPic(EntitiesUtil.assertNotNull(url));
        object.setPicNum(EntitiesUtil.assertNotNull(urls));
        object.setPay(talkPayed >= 1);
        object.setSupport(talkLiked >= 1);
        object.setVideo(mediaType == 2);
        object.setHideName("1".equals(isAnonymous));
        object.setProvinceName(EntitiesUtil.assertNotNull(province));
        object.setCityName(EntitiesUtil.assertNotNull(city));

        return object;
    }
}
