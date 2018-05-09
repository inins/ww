package com.wang.social.im.mvp.model.entities;

import com.frame.component.entities.FriendInfo;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 10:28
 * ============================================
 */
public class IndexFriendInfo extends FriendInfo implements IndexableEntity {

    @Override
    public String getFieldIndexBy() {
        return getNickname();
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        setNickname(indexField);
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }
}
