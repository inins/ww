package com.wang.social.personal.mvp.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccountBalance implements Serializable{

    /**
     * amount : 用户可提现金额
     * userId : 用户ID
     * amountDiamond : 用户剩余钻石
     * amountGemstone 宝石
     */

    private float amount;
    private int userId;
    private float amountDiamond;
    private float amountGemstone;

    public int getAmountDiamond() {
        return (int)amountDiamond;
    }

    public int getAmountGemstone() {
        return (int)amountGemstone;
    }
}
