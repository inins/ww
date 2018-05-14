package com.frame.component.entities.dto;

import com.frame.component.entities.AccountBalance;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class AccountBalanceDTO implements Mapper<AccountBalance> {
    private Integer amount;
    private Integer userId;
    private Integer amountDiamond;
    private Integer amountGemstone;

    @Override
    public AccountBalance transform() {
        AccountBalance object = new AccountBalance();

        object.setAmount(null == amount? 0 : amount);
        object.setUserId(null == userId? 0 : userId);
        object.setAmountDiamond(null == amountDiamond? 0 : amountDiamond);
        object.setAmountGemstone(null == amountGemstone? 0 : amountGemstone);

        return object;
    }
}
