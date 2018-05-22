package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.GroupJoinCheckResult;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-22 9:08
 * ============================================
 */
public class GroupJoinCheckResultDTO implements Mapper<GroupJoinCheckResult> {

    private String applyId;    //订单申请ID
    private int payState;    //0：不需要支付,1:需要调用支付接口
    private int gemstone;  //所需宝石数
    private boolean needValidation;   //是否需要群主同意  true/false

    @Override
    public GroupJoinCheckResult transform() {
        GroupJoinCheckResult checkResult = new GroupJoinCheckResult();
        checkResult.setApplyId(applyId == null ? "-1" : applyId);
        checkResult.setNeedPay(payState == 1);
        checkResult.setJoinCost(gemstone);
        checkResult.setValidation(needValidation);
        return checkResult;
    }
}