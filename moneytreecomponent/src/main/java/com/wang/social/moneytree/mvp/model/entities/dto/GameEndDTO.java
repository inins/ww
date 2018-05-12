package com.wang.social.moneytree.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.moneytree.mvp.model.entities.EntitiesUtil;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.model.entities.GameEndScore;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GameEndDTO implements Mapper<GameEnd> {
    private Integer diamond;
    private Integer joinNum;
    private Integer ranking;
    private Integer usedTimeLen;
    private Integer myGetDiamond;
    private List<GameEndScore> list;

    @Override
    public GameEnd transform() {
        GameEnd object = new GameEnd();

        object.setDiamond(EntitiesUtil.assertNotNull(diamond));
        object.setJoinNum(EntitiesUtil.assertNotNull(joinNum));
        object.setRanking(EntitiesUtil.assertNotNull(ranking));
        object.setUsedTimeLen(EntitiesUtil.assertNotNull(usedTimeLen));
        object.setMyGetDiamond(EntitiesUtil.assertNotNull(myGetDiamond));
        object.setList(null == list ? new ArrayList<>() : list);

        return object;
    }
}
