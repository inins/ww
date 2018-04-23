package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.acticity.PhotoActivity;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.wang.social.funshow.R2;

import java.util.ArrayList;

import butterknife.BindView;

public class FunshowDetailContentBoardController extends BaseController {

    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.bundleview)
    BundleImgView bundleview;

    public FunshowDetailContentBoardController(View root) {
        super(root);
//        int layout = R.layout.funshow_lay_detail_contentboard;
    }

    @Override
    protected void onInitCtrl() {
        FontUtils.boldText(textName);
        FontUtils.boldText(textTitle);
        bundleview.setOnBundleClickListener(new BundleImgView.OnBundleSimpleClickListener() {
            @Override
            public void onPhotoShowClick(String path, int position) {
                PhotoActivity.start(getContext(), bundleview.getResultsStrArray(), position);
            }
        });
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

    /////////////////////////////////////////

    public void changeBundleView(int colcount, float wihi){
        bundleview.setColcountWihi(colcount, wihi);
    }

    public int getBundleViewColCount(){
        return bundleview.getColcount();
    }
}
