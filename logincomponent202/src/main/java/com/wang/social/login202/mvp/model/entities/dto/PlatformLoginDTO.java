package com.wang.social.login202.mvp.model.entities.dto;

import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;
import com.wang.social.login202.mvp.model.entities.PlatformLogin;

import lombok.Data;

@Data
public class PlatformLoginDTO implements Mapper<PlatformLogin> {
    private Integer isNewUser;

    @Override
    public PlatformLogin transform() {
        PlatformLogin object = new PlatformLogin();

        object.setNewUser(EntitiesUtil.assertNotNull(isNewUser) == 1);

        return object;
    }
}
