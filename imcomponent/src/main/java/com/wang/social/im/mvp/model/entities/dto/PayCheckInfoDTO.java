package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.PayCheckInfo;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 17:03
 * ============================================
 */
public class PayCheckInfoDTO implements Mapper<PayCheckInfo> {

    //订单申请ID
    private String applyId;
    //0：需要支付, 1:无需支付
    private int applyState;

    @Override
    public PayCheckInfo transform() {
        PayCheckInfo checkInfo = new PayCheckInfo();
        checkInfo.setOrderId(applyId == null ? "" : applyId);
        if (applyState == 1){
            checkInfo.setCheckState(PayCheckInfo.STATUS_WITHOUT);
        }else {
            checkInfo.setCheckState(PayCheckInfo.STATUS_NEED);
        }
        return checkInfo;
    }
}