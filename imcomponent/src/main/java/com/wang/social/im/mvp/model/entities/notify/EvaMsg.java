package com.wang.social.im.mvp.model.entities.notify;

import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EvaMsg {


    /**
     * sendUserId : 10000
     * receiveUserId : 10001
     * msgContent : 对你的趣晒进行了评论
     * modePkId : 4
     * modeId : 67
     * modeDesc : http://resouce.dongdonfd-b85c-dc864c50383a.mp3
     * modeType : 7
     * state : 0
     * createTime : 1525240359000
     * sendNickname : 飘飘飘香
     * sendAvatar : http://resouce.dongd8258.jpg
     * msgId : 46
     */

    private int sendUserId;
    private int receiveUserId;
    private String msgContent;
    private int modePkId;
    private int modeId;
    private String modeDesc;
    private int modeType;
    private int state;
    private long createTime;
    private String sendNickname;
    private String sendAvatar;
    private int msgId;

    public CommonMsg trans2CommonMsg() {
        CommonMsg commonMsg = new CommonMsg();
        commonMsg.setId(msgId);
        commonMsg.setAvatar(sendAvatar);
        commonMsg.setPicUrl("");
        commonMsg.setName(sendNickname);
        commonMsg.setTime(createTime);
        commonMsg.setContent(msgContent);
        return commonMsg;
    }

    public static List<CommonMsg> tans2CommonMsgList(List<EvaMsg> list) {
        List<CommonMsg> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (EvaMsg bean : list) {
                toList.add(bean.trans2CommonMsg());
            }
        }
        return toList;
    }
}
