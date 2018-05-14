package com.wang.social.moneytree.mvp.model.entities;

import lombok.Data;

@Data
public class GameScore {
    private int gotDiamond;
    private String nickname;
    private int userId;
    private boolean isMyself;
}
