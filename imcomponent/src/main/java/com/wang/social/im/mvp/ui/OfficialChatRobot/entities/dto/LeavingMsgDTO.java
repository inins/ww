package com.wang.social.im.mvp.ui.OfficialChatRobot.entities.dto;

import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.ui.OfficialChatRobot.entities.LeavingMsg;

import lombok.Data;

@Data
public class LeavingMsgDTO implements Mapper<LeavingMsg> {
    Integer msgId;
    String msgContent;
    Integer msgState;
    Integer sendUserId;
    Integer receiveUserId;
    Long createTime;

    @Override
    public LeavingMsg transform() {
        LeavingMsg object = new LeavingMsg();

        object.setMsgId(EntitiesUtil.assertNotNull(msgId));
        object.setMsgContent(EntitiesUtil.assertNotNull(msgContent));
        object.setMsgState(EntitiesUtil.assertNotNull(msgState));
        object.setSendUserId(EntitiesUtil.assertNotNull(sendUserId));
        object.setReceiveUserId(EntitiesUtil.assertNotNull(receiveUserId));
        object.setCreateTime(EntitiesUtil.assertNotNull(createTime));

        return object;
    }
}
