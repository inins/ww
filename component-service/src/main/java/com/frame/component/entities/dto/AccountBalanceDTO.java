package com.frame.component.entities.dto;

import com.frame.component.entities.AccountBalance;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class AccountBalanceDTO implements Mapper<AccountBalance> {
    private Float amount;
    private Integer userId;
    private float amountDiamond;
    private float amountGemstone;

    @Override
    public AccountBalance transform() {
        AccountBalance object = new AccountBalance();

        object.setAmount(null == amount ? 0F : amount);
        object.setUserId(null == userId ? 0 : userId);
        object.setAmountDiamond(amountDiamond);
        object.setAmountGemstone(amountGemstone);

        return object;
    }
}
