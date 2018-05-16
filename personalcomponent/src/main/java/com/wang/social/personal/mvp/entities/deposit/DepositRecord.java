package com.wang.social.personal.mvp.entities.deposit;

import lombok.Data;

@Data
public class DepositRecord {

    /**
     * id : 提现ID
     * userId : 用户ID
     * alipayLogonid : 支付宝账号
     * realName : 真实姓名
     * price : 提现金额
     * createTime : 1512548223000
     * status : success
     */

    private int id;
    private int userId;
    private String alipayLogonid;
    private String realName;
    private float price;
    private long createTime;
    private String status;

    public boolean isSuccess() {
        return "success".equals(status);
    }

    public String getStatusText() {
        return isSuccess() ? "成功" : "失败";
    }
}
