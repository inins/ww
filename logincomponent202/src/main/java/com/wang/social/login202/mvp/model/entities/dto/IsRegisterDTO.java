package com.wang.social.login202.mvp.model.entities.dto;

import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;
import com.wang.social.login202.mvp.model.entities.IsRegisterVO;

public class IsRegisterDTO implements Mapper<IsRegisterVO> {
    private String mobile;
    private Integer isRegister;

    @Override
    public IsRegisterVO transform() {
        IsRegisterVO object = new IsRegisterVO();

        object.setMobile(EntitiesUtil.assertNotNull(mobile));
        object.setRegister(EntitiesUtil.assertNotNull(isRegister) == 1);

        return object;
    }
}
