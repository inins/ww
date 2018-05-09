package com.wang.social.home.mvp.ui.controller;

import android.view.View;

import com.frame.component.ui.base.BaseController;
import com.wang.social.home.R;

public class DetailTopicController extends BaseController {

    public DetailTopicController(View root) {
        super(root);
        int layout = R.layout.home_item_topic;
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
