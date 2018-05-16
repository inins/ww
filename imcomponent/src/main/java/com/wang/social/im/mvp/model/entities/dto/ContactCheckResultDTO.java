package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.ContactCheckResult;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 11:16
 * ============================================
 */
public class ContactCheckResultDTO implements Mapper<ContactCheckResult> {

    private String phone;
    private String userId;  //null 未加入往往  存在ID，已经加入往往
    private int isFriend;  //是否是好友 0:不是好友 1：已经是好友

    @Override
    public ContactCheckResult transform() {
        ContactCheckResult result = new ContactCheckResult();
        result.setFriend(isFriend == 1);
        result.setPhoneNumber(phone == null ? "" : phone);
        result.setUserId(userId);
        return result;
    }
}