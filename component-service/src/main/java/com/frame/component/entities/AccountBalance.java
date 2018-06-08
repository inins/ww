package com.frame.component.entities;

import lombok.Data;

@Data
public class AccountBalance {
    private float amount;
    private float amountGemstone;
    private int userId;
    private float amountDiamond;

    public int getAmountDiamond() {
        return (int) amountDiamond;
    }

    public int getAmountGemstone() {
        return (int)amountGemstone;
    }
}
