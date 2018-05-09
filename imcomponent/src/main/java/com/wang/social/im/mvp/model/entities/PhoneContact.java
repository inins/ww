package com.wang.social.im.mvp.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.yokeyword.indexablerv.IndexableEntity;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 16:12
 * ============================================
 */
@Data
@NoArgsConstructor
public class PhoneContact implements IndexableEntity {

    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 姓名
     */
    private String name;
    /**
     * 是否为好友关系
     */
    private boolean friend;
    /**
     * 是否已经加入往往
     */
    private boolean joined;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户头像
     */
    private String portrait;

    @Override
    public String getFieldIndexBy() {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }
}
