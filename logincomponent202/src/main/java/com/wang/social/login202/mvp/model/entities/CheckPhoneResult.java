package com.wang.social.login202.mvp.model.entities;

import lombok.Data;

@Data
public class CheckPhoneResult {
    /**
     * 参数名字	参数类型	说明	是否必须
     checkResult	Integer	检查结果(0：手机号已经绑定；1：手机号未绑定	√
     phone	String	手机号码	√
     */

    private int checkResult;
    private String phone;
}
