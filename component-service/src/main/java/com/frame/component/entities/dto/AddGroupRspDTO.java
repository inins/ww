package com.frame.component.entities.dto;

import com.frame.component.entities.AddGroupRsp;
import com.frame.http.api.Mapper;

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
