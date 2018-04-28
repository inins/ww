package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.CreateEnvelopResult;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-27 15:32
 * ============================================
 */
public class CreateEnvelopResultDTO implements Mapper<CreateEnvelopResult>{

    private long packId;

    @Override
    public CreateEnvelopResult transform() {
        CreateEnvelopResult result = new CreateEnvelopResult();
        result.setEnvelopId(packId);
        return result;
    }
}
