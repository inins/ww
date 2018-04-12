package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.view.WindowManager;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.wang.social.personal.R;

public class DepositePopupWindow extends BasePopupWindow {

    public DepositePopupWindow(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    @Override
    public int getLayout() {
        return R.layout.personal_pop_deposit;
    }

    @Override
    public void initBase() {

    }
}
