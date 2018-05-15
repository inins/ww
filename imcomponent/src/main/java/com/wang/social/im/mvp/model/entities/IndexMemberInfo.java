package com.wang.social.im.mvp.model.entities;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:33
 * ============================================
 */
public class IndexMemberInfo extends MemberInfo implements IndexableEntity {

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
