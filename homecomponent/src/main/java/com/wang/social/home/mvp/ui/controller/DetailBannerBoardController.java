package com.wang.social.home.mvp.ui.controller;

import android.view.View;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.view.ConerTextView;
import com.frame.component.view.bannerview.BannerView;
import com.frame.component.view.bannerview.Image;
import com.wang.social.home.R;
import com.wang.social.home.R2;

import java.util.ArrayList;

import butterknife.BindView;

public class DetailBannerBoardController extends BaseController {

    @BindView(R2.id.banner)
    BannerView banner;
    @BindView(R2.id.connertext_lable)
    ConerTextView connertextLable;

    public DetailBannerBoardController(View root) {
        super(root);
        int layout = R.layout.home_lay_carddetail_bannerboard;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
    }

    @Override
    protected void onInitData() {
        banner.setDatas(new ArrayList<Image>() {{
            add(new Image(ImageLoaderHelper.getRandomImg()));
            add(new Image(ImageLoaderHelper.getRandomImg()));
            add(new Image(ImageLoaderHelper.getRandomImg()));
            add(new Image(ImageLoaderHelper.getRandomImg()));
        }});
    }

    public BannerView getBannerView() {
        return banner;
    }
}
