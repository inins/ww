package com.frame.component.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/25.
 * 选择实体基类，需要被选中（单选或多选的实体都应该继承自该类）
 * 配合{@link com.frame.component.helper.SelectHelper}使用
 */

public class BaseSelectBean implements Serializable {

    //是否被选择
    protected boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
