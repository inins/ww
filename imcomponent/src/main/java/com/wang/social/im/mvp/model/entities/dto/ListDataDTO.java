package com.wang.social.im.mvp.model.entities.dto;

import com.frame.http.api.Mapper;
import com.wang.social.im.mvp.model.entities.ListData;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 9:42
 * ============================================
 */
public class ListDataDTO<T extends Mapper<R>, R> implements Mapper<ListData<R>> {

    private List<T> list;

    @Override
    public ListData<R> transform() {
        ListData<R> listData = new ListData();
        List<R> dataList = new ArrayList<>();
        if (list != null){
            for (T t : list){
                dataList.add(t.transform());
            }
        }
        listData.setList(dataList);
        return listData;
    }
}