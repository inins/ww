package com.wang.social.moneytree.mvp.model.entities;

import java.util.List;

import lombok.Data;

@Data
public class GameEnd {
    private int diamond;
    private int joinNum;
    private int ranking;
    private int usedTimeLen;
    private int myGetDiamond;
    private List<GameEndScore> list;
}
