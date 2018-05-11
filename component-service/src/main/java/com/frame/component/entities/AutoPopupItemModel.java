package com.frame.component.entities;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by CJ on 2017/2/5 0005.
 */

public class AutoPopupItemModel {

    private int icon;
    private int menuName;

    public AutoPopupItemModel(@DrawableRes int icon, @StringRes int menuName) {
        this.icon = icon;
        this.menuName = menuName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getMenuName() {
        return menuName;
    }

    public void setMenuName(int menuName) {
        this.menuName = menuName;
    }
}
