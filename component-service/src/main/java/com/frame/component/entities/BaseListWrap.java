package com.frame.component.entities;

import java.util.List;

import lombok.Data;

@Data
public class BaseListWrap<T> {
    private int total;
    private int size;
    private int pages;
    private int current;
    private List<T> list;

    //代言收益
    private String userCode;
    private int userTotalMoney;
}
