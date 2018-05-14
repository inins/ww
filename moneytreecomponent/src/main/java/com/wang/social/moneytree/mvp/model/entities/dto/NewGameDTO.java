package com.wang.social.moneytree.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.moneytree.mvp.model.entities.EntitiesUtil;
import com.wang.social.moneytree.mvp.model.entities.NewGame;

import lombok.Data;

@Data
public class NewGameDTO implements Mapper<NewGame> {
    private Integer applyId;
    private Integer diamond;
    private Integer payState;
    private Integer state;
    private Integer roomId;
    private Integer gameId;
    private Integer balance;

    @Override
    public NewGame transform() {
        NewGame o = new NewGame();

        o.setApplyId(EntitiesUtil.assertNotNull(applyId));
        o.setDiamond(EntitiesUtil.assertNotNull(diamond));
        o.setPayState(EntitiesUtil.assertNotNull(payState));
        o.setRoomId(EntitiesUtil.assertNotNull(roomId));
        o.setGameId(EntitiesUtil.assertNotNull(gameId));
        o.setBalance(EntitiesUtil.assertNotNull(balance));

        return o;
    }
}
