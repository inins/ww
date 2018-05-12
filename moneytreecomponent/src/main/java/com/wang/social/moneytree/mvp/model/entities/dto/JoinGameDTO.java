package com.wang.social.moneytree.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.moneytree.mvp.model.entities.EntitiesUtil;
import com.wang.social.moneytree.mvp.model.entities.JoinGame;

import lombok.Data;

@Data
public class JoinGameDTO implements Mapper<JoinGame> {
    private Integer applyId;
    private Integer diamond;
    private Integer payState;
    private Integer state;
    private Integer gameId;
    private Integer balance;

    @Override
    public JoinGame transform() {
        JoinGame object = new JoinGame();

        object.setApplyId(EntitiesUtil.assertNotNull(applyId));
        object.setDiamond(EntitiesUtil.assertNotNull(diamond));
        object.setPayState(EntitiesUtil.assertNotNull(payState));
        object.setState(EntitiesUtil.assertNotNull(state));
        object.setGameId(EntitiesUtil.assertNotNull(gameId));
        object.setBalance(EntitiesUtil.assertNotNull(balance));

        return object;
    }
}
