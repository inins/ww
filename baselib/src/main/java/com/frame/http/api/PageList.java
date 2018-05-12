package com.frame.http.api;

import java.util.List;

/**
 * Author: ChenJing
 * Date: 2017-06-19 下午 5:27
 * Version: 1.0
 */

public class PageList<T> {

    private int maxPage;
    private int currentPage;
    private int total;
    private int current;
    private String orderField;
    private List<T> records;

    public List<T> getList() {
        return records;
    }

    public void setList(List<T> list) {
        this.records = list;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
