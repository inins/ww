package com.wang.social.funshow.mvp.ui.dialog;

import android.content.Context;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.wang.social.funshow.R;

public class MorePopupWindow extends BasePopupWindow {

    public MorePopupWindow(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    @Override
    public int getLayout() {
        return R.layout.funshow_pop_dislike;
    }

    @Override
    public void initBase() {

    }
}
