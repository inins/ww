package com.wang.social.personal.mvp.entities;

import com.frame.component.entities.BaseSelectBean;
import com.frame.component.helper.SelectHelper;
import com.frame.utils.StrUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ShowListCate extends BaseSelectBean implements Serializable {

    private String title;
    private String paraName;
    private List<ShowListGroup> groupList;
    private boolean isShow;


    public ShowListCate() {
    }

    public ShowListCate(String title, String paraName, List<ShowListGroup> groupList) {
        this.title = title;
        this.paraName = paraName;
        this.groupList = groupList;
    }

    //////////////////////////////////////////////////

    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public void setSelect(boolean select) {
        isSelect = select;
        SelectHelper.selectAllSelectBeans(groupList, select);
    }

    public void setSelectOnly(boolean select) {
        isSelect = select;
    }

    //////////////////////////////////////////////////

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return groupList != null ? groupList.size() : 0;
    }

    public int getSelectChildCount() {
        List<ShowListGroup> selectBeans = SelectHelper.getSelectBeans(groupList);
        return selectBeans != null ? selectBeans.size() : 0;
    }

    public List<ShowListGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<ShowListGroup> groupList) {
        this.groupList = groupList;
    }
}
