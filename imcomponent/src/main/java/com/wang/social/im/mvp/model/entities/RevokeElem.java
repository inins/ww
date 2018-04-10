package com.wang.social.im.mvp.model.entities;

import com.wang.social.im.enums.CustomElemType;

import lombok.Getter;
import lombok.Setter;

/**
 * ============================================
 * 撤回消息
 * <p>
 * Create by ChenJing on 2018-04-10 9:30
 * ============================================
 */
public class RevokeElem {

    private String type;
    @Getter
    @Setter
    private String sender;

    public void setType(CustomElemType elemType) {
        this.type = elemType.getValue();
    }

    public CustomElemType getType() {
        return CustomElemType.valueOf(type);
    }
}