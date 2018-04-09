package com.wang.social.personal.mvp.entities.lable;

import java.util.List;

public class LableWrap {


    private int total;
    private int size;
    private int pages;
    private int current;
    private List<Lable> list;

    public List<Lable> getList() {
        return list;
    }

    public void setList(List<Lable> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
