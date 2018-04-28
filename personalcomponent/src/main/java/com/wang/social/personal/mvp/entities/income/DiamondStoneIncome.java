package com.wang.social.personal.mvp.entities.income;

import lombok.Data;

@Data
public class DiamondStoneIncome {

    /**
     * dateTime : 1524901362000
     * orderNo : 132
     * incomeId : 128
     * amountMoney : null
     * type : 支出
     * amountDiamond : -100.0
     * content : 钻石兑换
     * gemstone : null
     */

    private long dateTime;
    private int orderNo;
    private int incomeId;
    private Object amountMoney;
    private String type;
    private String content;
    private float amountDiamond;
    private float gemstone;
}
