package com.wang.social.home.mvp.ui.controller;

import android.view.View;
import android.widget.TextView;

import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.wang.social.home.R;
import com.wang.social.home.R2;

import butterknife.BindView;

public class HomeFunshowController extends BaseController {

    public HomeFunshowController(View root) {
        super(root);
        int layout = R.layout.home_lay_funshow_item;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
    }

    @Override
    protected void onInitData() {
    }
}
