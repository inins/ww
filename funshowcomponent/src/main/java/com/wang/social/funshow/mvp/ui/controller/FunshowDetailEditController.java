package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.frame.component.ui.base.BaseController;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;

import butterknife.BindView;

public class FunshowDetailEditController extends BaseController {

    @BindView(R2.id.img_emoji)
    ImageView imgEmoji;
    @BindView(R2.id.edit_eva)
    EditText editEva;

    public FunshowDetailEditController(View root) {
        super(root);
        int layout = R.layout.funshow_lay_detail_edit;
    }

    @Override
    protected void onInitCtrl() {
    }

    @Override
    protected void onInitData() {
    }
}
