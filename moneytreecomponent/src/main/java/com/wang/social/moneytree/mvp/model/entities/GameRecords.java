package com.wang.social.moneytree.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class GameRecords {
    private int total;
    private int size;
    private int pages;
    private int current;
    List<GameRecord> list;
}
