package com.wang.social.login202.mvp.model.entities.dto;


import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;
import com.wang.social.login202.mvp.model.entities.CheckPhoneResult;

public class CheckPhoneResultDTO implements Mapper<CheckPhoneResult> {
    /**
     * 参数名字	参数类型	说明	是否必须
     checkResult	Integer	检查结果(0：手机号已经绑定；1：手机号未绑定	√
     phone	String	手机号码	√
     */

    private Integer checkResult;
    private String phone;

    @Override
    public CheckPhoneResult transform() {
        CheckPhoneResult object = new CheckPhoneResult();

        object.setCheckResult(EntitiesUtil.assertNotNull(checkResult));
        object.setPhone(EntitiesUtil.assertNotNull(phone));


        return object;
    }
}
