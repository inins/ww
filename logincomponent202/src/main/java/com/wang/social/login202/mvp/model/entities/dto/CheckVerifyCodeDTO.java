package com.wang.social.login202.mvp.model.entities.dto;

import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;
import com.wang.social.login202.mvp.model.entities.CheckVerifyCode;

public class CheckVerifyCodeDTO implements Mapper<CheckVerifyCode> {
    private String mobile;
    private Integer checkVerificationCode;
    private String message;

    @Override
    public CheckVerifyCode transform() {
        CheckVerifyCode object = new CheckVerifyCode();

        object.setMobile(EntitiesUtil.assertNotNull(mobile));
        object.setOK(EntitiesUtil.assertNotNull(checkVerificationCode) == 1);
        object.setMessage(EntitiesUtil.assertNotNull(message));

        return object;
    }
}
