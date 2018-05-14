package com.frame.http.api;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页列表数据
 * <p>
 * Author: ChenJing
 * Date: 2017-04-24 下午 8:14
 * Version: 1.0
 * Desc: T：转换类， R：转换目标类
 */

public class PageListDTO<T extends Mapper<R>, R> implements Mapper<PageList<R>> {

    private int total;
    private int pages;
    private int current;
    private String orderByField;
    private List<T> list;

    @Override
    public PageList<R> transform() {
        PageList<R> listResult = new PageList<R>();
        listResult.setMaxPage(pages);
        listResult.setCurrentPage(current);
        listResult.setCurrent(current);
        listResult.setOrderField(orderByField);
        List<R> listR = new ArrayList<>();
        if (list != null) {
            for (T t : list) {
                listR.add(t.transform());
            }
        }
        listResult.setList(listR);
        listResult.setTotal(total);
        return listResult;
    }
}
