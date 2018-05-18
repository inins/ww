package com.frame.component.entities.dto;

import com.frame.component.entities.NewMoneyTreeGame;
import com.frame.component.utils.EntitiesUtil;
import com.frame.http.api.Mapper;

import lombok.Data;

@Data
public class NewMoneyTreeGameDTO implements Mapper<NewMoneyTreeGame> {
    private Integer applyId;
    private Integer diamond;
    private Integer payState;
    private Integer state;
    private Integer roomId;
    private Integer gameId;
    private Integer balance;

    @Override
    public NewMoneyTreeGame transform() {
        NewMoneyTreeGame o = new NewMoneyTreeGame();

        o.setApplyId(EntitiesUtil.assertNotNull(applyId));
        o.setDiamond(EntitiesUtil.assertNotNull(diamond));
        o.setPayState(EntitiesUtil.assertNotNull(payState));
        o.setRoomId(EntitiesUtil.assertNotNull(roomId));
        o.setGameId(EntitiesUtil.assertNotNull(gameId));
        o.setBalance(EntitiesUtil.assertNotNull(balance));

        return o;
    }
}
