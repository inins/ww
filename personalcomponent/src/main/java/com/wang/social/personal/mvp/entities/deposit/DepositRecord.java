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
        switch (status) {
            case "processing":
                return "审核中";
            case "unpaid":
                return "未支付";
            case "resusal_pay":
                return "拒绝支付";
            case "success":
                return "审核成功";
            case "fail":
                return "审核失败";
            default:
                return "";
        }
    }
}
