package com.wang.social.personal.mvp.entities;

import com.frame.component.entities.BaseSelectBean;
import com.frame.component.helper.SelectHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ShowListCate implements Serializable {

    private String title;
    private int count;
    private List<ShowListGroup> groupList;
    private boolean isShow;

    //////////////////////////////////////////////////

    public boolean isSelect() {
        return SelectHelper.isSelectAll(groupList);
    }

    public void setSelect(boolean select) {
        SelectHelper.selectAllSelectBeans(groupList, select);
    }

    //////////////////////////////////////////////////

    public ShowListCate() {
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public ShowListCate(String title, List<ShowListGroup> groupList) {
        this.title = title;
        this.groupList = groupList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ShowListGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<ShowListGroup> groupList) {
        this.groupList = groupList;
    }
}
