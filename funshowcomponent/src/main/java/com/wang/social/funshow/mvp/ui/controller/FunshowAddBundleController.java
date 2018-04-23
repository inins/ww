package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.wang.social.funshow.R2;

import java.util.ArrayList;

import butterknife.BindView;

public class FunshowAddBundleController extends BaseController {

    @BindView(R2.id.bundleview)
    BundleImgView bundleview;

    public FunshowAddBundleController(View root) {
        super(root);
        //int layout = R.layout.funshow_lay_add_bundle;
    }

    @Override
    protected void onInitCtrl() {
        bundleview.setMaxcount(9);
    }

    @Override
    protected void onInitData() {
        bundleview.setPhotos(new ArrayList<BundleImgEntity>() {{
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
            add(new BundleImgEntity(ImageLoaderHelper.getRandomImg()));
        }});
    }
}
