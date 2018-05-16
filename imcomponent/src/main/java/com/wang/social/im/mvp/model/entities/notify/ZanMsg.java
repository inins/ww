package com.wang.social.im.mvp.model.entities.notify;

import com.frame.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ZanMsg {

    /**
     * sendUserId : 10000
     * receiveUserId : 10001
     * msgContent : 对你的话题进行了点赞
     * modePkId : 0
     * modeId : 4
     * modeName : 我新发的标题
     * modeType : 1
     * state : 0
     * createTime : 1525229849000
     * sendNickname : 飘飘飘香
     * sendAvatar : http://resouce.dongdona63-4984-8519-8ee0500d8258.jpg
     * msgId : 40
     */

    private int sendUserId;
    private int receiveUserId;
    private String msgContent;
    private int modePkId;
    private int modeId;
    private String modeName;
    private int modeType;
    private int state;
    private long createTime;
    private String sendNickname;
    private String sendAvatar;
    private int msgId;

    /**
     *     private int id;
     private String avatar;
     private String picUrl;
     private String name;
     private long time;
     private String content;
     * @return
     */
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

    public static List<CommonMsg> tans2CommonMsgList(List<ZanMsg> list) {
        List<CommonMsg> toList = new ArrayList<>();
        if (!StrUtil.isEmpty(list)) {
            for (ZanMsg bean : list) {
                toList.add(bean.trans2CommonMsg());
            }
        }
        return toList;
    }
}
