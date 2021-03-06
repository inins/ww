package com.frame.component.entities;

import lombok.Data;

@Data
public class IsShoppingRsp {
    /**
     * "isFree": "是否收费（0免费 1收费）",
     * "price": "收费价格,宝石数",
     * "isShopping": "是否需要购买（0 无需购买 1 购买）"
     */
    private int isFree;
    private int price;
    private int isShopping;


    //是否免费
    public boolean isFree() {
        return isFree == 0;
    }

    //是否已经购买
    public boolean isPay() {
        return isShopping == 0;
    }
}
