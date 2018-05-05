package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.CreateGroupResult;

import java.util.Map;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-04 10:20
 * ============================================
 */
public class CreateGroupResultDTO implements Mapper<CreateGroupResult> {

    private String groupId;

    @Override
    public CreateGroupResult transform() {
        CreateGroupResult result = new CreateGroupResult();
        result.setId(groupId);
        return result;
    }
}
