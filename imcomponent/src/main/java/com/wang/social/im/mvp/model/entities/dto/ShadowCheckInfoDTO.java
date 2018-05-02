package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.ShadowCheckInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 17:03
 * ============================================
 */
public class ShadowCheckInfoDTO implements Mapper<ShadowCheckInfo> {

    //订单申请ID
    private String applyId;
    //0：需要支付, 1:无需支付
    private int applyState;

    @Override
    public ShadowCheckInfo transform() {
        ShadowCheckInfo checkInfo = new ShadowCheckInfo();
        checkInfo.setOrderId(applyId == null ? "" : applyId);
        if (applyState == 0){
            checkInfo.setCheckState(ShadowCheckInfo.STATUS_WITHOUT);
        }else {
            checkInfo.setCheckState(ShadowCheckInfo.STATUS_NEED);
        }
        return checkInfo;
    }
}