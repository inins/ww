package com.wang.social.im.mvp.model.entities;

import lombok.Data;

@Data
public class AddGroupRsp {
    /**
     state:1,	//1:加入群成功  2：已经发送加群申请
     */
    private int state;
}
