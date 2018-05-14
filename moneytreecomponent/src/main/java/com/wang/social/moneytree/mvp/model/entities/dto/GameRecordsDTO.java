package com.wang.social.moneytree.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.moneytree.mvp.model.entities.EntitiesUtil;
import com.wang.social.moneytree.mvp.model.entities.GameRecord;
import com.wang.social.moneytree.mvp.model.entities.GameRecords;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GameRecordsDTO implements Mapper<GameRecords> {
    private Integer total;
    private Integer size;
    private Integer pages;
    private Integer current;
    List<GameRecord> list;

    @Override
    public GameRecords transform() {
        GameRecords o = new GameRecords();
        o.setTotal(EntitiesUtil.assertNotNull(total));
        o.setSize(EntitiesUtil.assertNotNull(size));
        o.setPages(EntitiesUtil.assertNotNull(pages));
        o.setCurrent(EntitiesUtil.assertNotNull(current));
        o.setList(null == list ? new ArrayList<>() :  list);

        return o;
    }
}
