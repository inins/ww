package com.frame.component.entities;

import lombok.Data;

@Data
public class AddGroupApplyRsp {
    /**
     applyId:11,	//订单申请ID
     payState:0,    //0：不需要支付,1:需要调用支付接口
     gemstone:100,  //所需宝石数
     needValidation:true   //是否需要群主同意  true/false
     */
    private int applyId;
    private int payState;
    private int gemstone;
    private boolean needValidation;
}
