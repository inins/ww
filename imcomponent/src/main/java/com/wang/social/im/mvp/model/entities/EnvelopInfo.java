package com.wang.social.im.mvp.model.entities;

import com.tencent.imsdk.TIMManager;

import lombok.Data;

/**
 * ============================================
 * 红包信息
 * <p>
 * Create by ChenJing on 2018-04-24 14:31
 * ============================================
 */
@Data
public class EnvelopInfo {

    public enum Status{
        /*
            还可以领取
         */
        LIVING,
        /*
            过期了
         */
        OVERDUE,
        /*
            抢完了
         */
        EMPTY
    }

    public enum EnvelopType{

        /*
            个人红包
         */
        PRIVATE,
        /*
            等额红包
         */
        EQUAL,
        /*
            拼手气红包
         */
        SPELL
    }

    private long envelopId;
    private Status status;
    private EnvelopType type;
    private String fromUid;
    private String fromPortrait;
    private String fromNickname;
    private String message;
    private int gotDiamond;

    /**
     * 检测是否是自己发的红包
     * @return
     */
    public boolean isSelf(){
        return TIMManager.getInstance().getLoginUser().equals(fromUid);
    }
}