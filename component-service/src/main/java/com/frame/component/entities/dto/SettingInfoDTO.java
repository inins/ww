package com.frame.component.entities.dto;

import com.frame.component.entities.SettingInfo;
import com.frame.http.api.Mapper;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-01 11:34
 * ============================================
 */
public class SettingInfoDTO implements Mapper<SettingInfo> {

    private int pushFlag; //推送开关 0表示推送，1表示不离线推送

    @Override
    public SettingInfo transform() {
        SettingInfo info = new SettingInfo();
        info.setPushEnable(pushFlag == 0);
        return info;
    }
}