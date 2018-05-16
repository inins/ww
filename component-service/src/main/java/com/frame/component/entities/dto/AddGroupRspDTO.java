package com.frame.component.entities.dto;

import com.frame.component.entities.AddGroupRsp;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class AddGroupRspDTO implements Mapper<AddGroupRsp> {
    /**
     state:1,	//1:加入群成功  2：已经发送加群申请
     */
    private Integer applyId;
    private Integer applyState;

    @Override
    public AddGroupRsp transform() {
        AddGroupRsp object = new AddGroupRsp();

        object.setApplyId(EntitiesUtil.assertNotNull(applyId));
        object.setApplyState(EntitiesUtil.assertNotNull(applyState));

        return object;
    }
}
