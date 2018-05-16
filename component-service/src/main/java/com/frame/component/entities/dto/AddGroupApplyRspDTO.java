package com.frame.component.entities.dto;

import com.frame.component.entities.AddGroupApplyRsp;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class AddGroupApplyRspDTO implements Mapper<AddGroupApplyRsp> {
    /**
     applyId:11,	//订单申请ID
     payState:0,    //0：不需要支付,1:需要调用支付接口
     gemstone:100,  //所需宝石数
     needValidation:true   //是否需要群主同意  true/false
     */
    private Integer applyId;
    private Integer payState;
    private Integer gemstone;
    private Boolean needValidation;

    private int assertNotNull(Integer v) { return v == null ? 0 : v; }

    @Override
    public AddGroupApplyRsp transform() {
        AddGroupApplyRsp object = new AddGroupApplyRsp();

        object.setApplyId(assertNotNull(applyId));
        object.setPayState(assertNotNull(payState));
        object.setGemstone(assertNotNull(gemstone));
        object.setNeedValidation(null == needValidation ? false : needValidation);

        return object;
    }
}
