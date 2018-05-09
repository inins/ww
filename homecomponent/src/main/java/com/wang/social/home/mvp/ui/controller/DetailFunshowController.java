package com.wang.social.home.mvp.ui.controller;

import android.view.View;

import com.frame.component.ui.base.BaseController;
import com.wang.social.home.R;

public class DetailFunshowController extends BaseController {

    public DetailFunshowController(View root) {
        super(root);
        int layout = R.layout.home_lay_funshow_item_noheader;
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
