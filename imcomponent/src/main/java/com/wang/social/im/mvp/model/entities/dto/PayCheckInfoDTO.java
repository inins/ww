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
    //是否允许创建觅聊， 1：允许，2：不允许（当创建觅聊的时候才有此字段）
    private int allowCreate;

    @Override
    public PayCheckInfo transform() {
        PayCheckInfo checkInfo = new PayCheckInfo();
        checkInfo.setApplyId(applyId == null ? "" : applyId);
        if (applyState == 1) {
            checkInfo.setCheckState(PayCheckInfo.STATUS_WITHOUT);
        } else {
            checkInfo.setCheckState(PayCheckInfo.STATUS_NEED);
        }
        checkInfo.setAllowCreateTeam(allowCreate == 1);
        return checkInfo;
    }
}