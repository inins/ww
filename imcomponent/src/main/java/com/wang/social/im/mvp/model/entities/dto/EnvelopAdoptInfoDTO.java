package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-27 10:59
 * ============================================
 */
public class EnvelopAdoptInfoDTO implements Mapper<EnvelopAdoptInfo> {

    //领取红包记录ID
    private long receiveId;
    //红包ID
    private long packId;
    private String userId;
    private String nickname;
    private String avatar;
    //获得钻石值
    private int diamond;
    //是否为手气最佳  0：非手气最佳、 1：手气最佳
    private int isLucky;
    //领取时间
    private long receiveTime;
    //创建时间
    private long updateTime;

    @Override
    public EnvelopAdoptInfo transform() {
        EnvelopAdoptInfo adoptInfo = new EnvelopAdoptInfo();
        adoptInfo.setLucky(isLucky == 1);
        adoptInfo.setGotDiamondNumber(diamond);
        adoptInfo.setNickname(nickname == null ? "" : nickname);
        adoptInfo.setPortrait(avatar == null ? "" : avatar);
        adoptInfo.setUserId(userId == null ? "0" : avatar);
        adoptInfo.setTime(receiveTime);
        return adoptInfo;
    }
}