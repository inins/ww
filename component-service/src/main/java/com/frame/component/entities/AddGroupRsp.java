package com.frame.component.entities;

import lombok.Data;

@Data
public class AddGroupRsp {
    /**
     state:1,	//1:加入群成功  2：已经发送加群申请
     {
     │         "applyId": 30,
     │         "applyState": 0
     │     }
     */
    private int applyId;
    private int applyState;
}
