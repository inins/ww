package com.frame.component.entities;

import lombok.Data;

@Data
public class AccountBalance {
    private float amount;
    private int amountGemstone;
    private int userId;
    private int amountDiamond;
}
