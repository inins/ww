package com.wang.social.personal.mvp.entities.recharge;

import lombok.Data;

@Data
public class Recharge {

    /**
     * id : 1
     * platform : android
     * packageName : 300钻值
     * diamondNum : 300
     * price : 3
     * productId :
     */

    private int id;
    private String platform;
    private String packageName;
    private int diamondNum;
    private float price;
    private String productId;

}
