package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.ShareInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 17:46
 * ============================================
 */
public class ShareInfoDTO implements Mapper<ShareInfo> {

    private int shareCount;
    private int totalMoney;
    private int todayMoney;

    @Override
    public ShareInfo transform() {
        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setShareCount(shareCount);
        shareInfo.setTotalIncome(totalMoney);
        shareInfo.setTodayIncome(todayMoney);
        return shareInfo;
    }
}