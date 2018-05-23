package com.wang.social.topic.mvp.model.entities;

import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class CommentRspDTO implements Mapper<CommentRsp> {
    private int count;
    private Comments page;

    @Override
    public CommentRsp transform() {
        CommentRsp object = new CommentRsp();

        object.setCount(EntitiesUtil.assertNotNull(count));
        object.setPage(null == page ? new Comments() : page);

        return object;
    }
}
