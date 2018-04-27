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

    private long envelopId; //红包ID
    private Status status; //红包状态
    private EnvelopType type; //红包类型
    private String fromUid; //发送人用户ID
    private String fromPortrait; //发送人头像
    private String fromNickname; //发送人昵称
    private String message; //消息
    private int diamond; //红包总钻石数
    private int gotDiamond; //获得钻石数
    private int lastDiamond; //剩余钻石数
    private int count; //总个数
    private int lastCount; //剩余个数

    /**
     * 检测是否是自己发的红包
     * @return
     */
    public boolean isSelf(){
        return TIMManager.getInstance().getLoginUser().equals(fromUid);
    }
}