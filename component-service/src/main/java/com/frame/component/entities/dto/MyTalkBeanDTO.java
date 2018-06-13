package com.frame.component.entities.dto;

import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import java.util.List;

import lombok.Data;

@Data
public class MyTalkBeanDTO implements Mapper<FunshowBean> {
    private Long birthday;
    private String address;
    private Integer supportTotal;
    private String cityCode;
    private String provinceCode;
    private Integer isSupport;
    private Integer creatorId;
    private String showOrHide;
    private Integer picNum;
    private Integer commentTotal;
    private Integer share_total;
    private Integer showPicMediaType;
    private Integer readTotal;
    private String avatar;
    private String content;
    private Integer relateState;
    private Integer gemstone;
    private String cityName;
    private Long createTime;
    private String showPic;
    private String nickname;
    private Integer id;
    private String provinceName;

    @Override
    public FunshowBean transform() {
        FunshowBean object = new FunshowBean();

        object.setId(EntitiesUtil.assertNotNull(id));
        object.setUserId(EntitiesUtil.assertNotNull(creatorId));
        object.setCreateTime(EntitiesUtil.assertNotNull(createTime));
        object.setNickname(EntitiesUtil.assertNotNull(nickname));
        object.setAvatar(EntitiesUtil.assertNotNull(avatar));
        object.setContent(EntitiesUtil.assertNotNull(content));
        object.setShareTotal(EntitiesUtil.assertNotNull(readTotal));
        object.setCommentTotal(EntitiesUtil.assertNotNull(commentTotal));
        object.setSupportTotal(EntitiesUtil.assertNotNull(supportTotal));
        object.setShareTotal(EntitiesUtil.assertNotNull(share_total));
        object.setFree(EntitiesUtil.assertNotNull(relateState) == 0);
        object.setPrice(EntitiesUtil.assertNotNull(gemstone));
        object.setShowPic(EntitiesUtil.assertNotNull(showPic));
        object.setPicNum(EntitiesUtil.assertNotNull(picNum));
        object.setPay(true);
        object.setSupport(EntitiesUtil.assertNotNull(isSupport) >= 1);
        object.setVideo(EntitiesUtil.assertNotNull(showPicMediaType) == 2);
        object.setHideName(false);
        object.setProvinceName(EntitiesUtil.assertNotNull(provinceName));
        object.setCityName(EntitiesUtil.assertNotNull(cityName));

        return object;
    }
}
