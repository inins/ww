package com.wang.social.im.mvp.ui.OfficialChatRobot.entities;

import lombok.Data;

@Data
public class LeavingMsg {
    /**
     msgId	int	消息Id
     msgContent	String	消息正文
     msgState	int	消息状态：0 未读；1：已读；
     sendUserId	int	发送者用户ID
     receiveUserId	int	接收者ID
     createTime	Date	消息生成时间
     */
    int msgId;
    String msgContent;
    int msgState;
    int sendUserId;
    int receiveUserId;
    long createTime;
}
