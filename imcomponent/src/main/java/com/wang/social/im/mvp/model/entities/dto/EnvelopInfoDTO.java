package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;

/**
 * ============================================
 * 由此类将接口返回数据转换成界面展示数据{@link EnvelopInfo}
 * <p>
 * Create by ChenJing on 2018-04-27 11:28
 * ============================================
 */
public class EnvelopInfoDTO implements Mapper<EnvelopInfo> {

    //红包ID
    private long packId;
    //红包类型 0: 一对一红包 1：普通红包 2： 拼手气红包
    private int packType;
    //消息
    private String remark;
    //红包剩余领取钻石数
    private int lastDiamond;
    //红包剩余领取个数
    private int lastCount;
    //发红包用户ID
    private String userId;
    //发红包用户昵称
    private String nickname;
    //发红包用户头像
    private String avatar;
    //红包个数
    private int packCount;
    //红包总钻石数
    private int diamond;
    //领取到的钻石数
    private int receiveDiamond;
    //红包状态 1 可以领取; 2 已经领完; 3 已经过期
    private int status;
    //花费时间
    private long spendTime;

    @Override
    public EnvelopInfo transform() {
        EnvelopInfo envelopInfo = new EnvelopInfo();
        envelopInfo.setEnvelopId(packId);
        switch (packType) {
            case 0:
                envelopInfo.setType(EnvelopInfo.EnvelopType.PRIVATE);
                break;
            case 1:
                envelopInfo.setType(EnvelopInfo.EnvelopType.EQUAL);
                break;
            case 2:
                envelopInfo.setType(EnvelopInfo.EnvelopType.SPELL);
                break;
        }
        envelopInfo.setMessage(remark == null ? "" : remark);
        envelopInfo.setDiamond(diamond);
        envelopInfo.setGotDiamond(receiveDiamond);
        envelopInfo.setLastDiamond(lastDiamond);
        envelopInfo.setCount(packCount);
        envelopInfo.setLastCount(lastCount);
        envelopInfo.setFromUid(userId);
        envelopInfo.setFromNickname(nickname == null ? "" : nickname);
        envelopInfo.setFromPortrait(avatar == null ? "" : avatar);
        switch (status) {
            case 1:
                envelopInfo.setStatus(EnvelopInfo.Status.LIVING);
                break;
            case 2:
                envelopInfo.setStatus(EnvelopInfo.Status.EMPTY);
                break;
            case 3:
                envelopInfo.setStatus(EnvelopInfo.Status.OVERDUE);
                break;
        }
        envelopInfo.setSpendTime(spendTime);
        return envelopInfo;
    }
}
