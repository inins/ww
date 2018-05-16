package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.AddGroupRsp;

import lombok.Data;

@Data
public class AddGroupRspDTO implements Mapper<AddGroupRsp> {
    /**
     state:1,	//1:加入群成功  2：已经发送加群申请
     */
    private Integer state;

    @Override
    public AddGroupRsp transform() {
        AddGroupRsp object = new AddGroupRsp();

        object.setState(state == null ?  0 : state);

        return object;
    }
}
