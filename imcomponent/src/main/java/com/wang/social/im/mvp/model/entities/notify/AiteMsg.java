package com.wang.social.im.mvp.model.entities.notify;

import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AiteMsg {


    /**
     * sendUserId : 10012
     * receiveUserId : 10001
     * msgContent : 发布趣晒@了你
     * modeId : 44
     * modeDesc : https://tse4-mm.cn.bing.net/th?id=OIP.rfU4rTmf0ZhSLLgIULkw3QHaEo&p=0&o=5&pid=1.1
     * modeType : 9
     * state : 0
     * createTime : 1525244205000
     * sendNickname : _JJ
     * sendAvatar : http://resouce.dongdongwedding.com/wangwang_2018-04-17_a697c09b-d9a9-4535-9624-cb9420d5a3a3.jpg
     * msgId : 48
     */

    private int sendUserId;
    private int receiveUserId;
    private String msgContent;
    private int modeId;
    private int modePkId;
    private String modeDesc;
    private int modeType;
    private int state;
    private long createTime;
    private String sendNickname;
    private String sendAvatar;
    private int msgId;
    private int readState;

    public boolean isRead() {
        return readState == 1;
    }

    public CommonMsg trans2CommonMsg() {
        CommonMsg commonMsg = new CommonMsg();
        commonMsg.setId(msgId);
        commonMsg.setAvatar(sendAvatar);
        commonMsg.setPicUrl(modeDesc);
        commonMsg.setName(sendNickname);
        commonMsg.setTime(createTime);
        commonMsg.setContent("发布趣晒-@了你");
        commonMsg.setModeType(modeType);
        commonMsg.setModeId(modeId);
        commonMsg.setModePkId(modePkId);
        commonMsg.setReadState(readState);
        return commonMsg;
    }

    public static List<CommonMsg> tans2CommonMsgList(List<AiteMsg> list) {
        List<CommonMsg> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (AiteMsg bean : list) {
                toList.add(bean.trans2CommonMsg());
            }
        }
        return toList;
    }
}
