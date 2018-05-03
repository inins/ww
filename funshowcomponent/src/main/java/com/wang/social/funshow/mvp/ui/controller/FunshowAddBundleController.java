package com.wang.social.funshow.mvp.ui.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.frame.entities.EventBean;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.helper.VideoPhotoHelperEx;
import com.wang.social.funshow.mvp.entities.funshow.Pic;
import com.wang.social.location.mvp.model.entities.LocationInfo;
import com.wang.social.pictureselector.helper.PhotoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FunshowAddBundleController extends FunshowAddBaseController implements PhotoHelper.OnPhotoCallback {

    @BindView(R2.id.bundleview)
    BundleImgView bundleview;
    @BindView(R2.id.text_position)
    TextView textPosition;

    private VideoPhotoHelperEx photoHelperEx;
    private int MaxPhotoCount = 9;

    private LocationInfo location;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_LOCATION_SELECT:
                location = (LocationInfo) event.get("location");
                textPosition.setVisibility(View.VISIBLE);
                textPosition.setText(location.getProvince() + location.getCity());
                break;
        }
    }

    public FunshowAddBundleController(View root) {
        super(root);
        int layout = R.layout.funshow_lay_add_bundle;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        photoHelperEx = VideoPhotoHelperEx.newInstance((Activity) getContext(), this);
        bundleview.setMaxcount(9);
        bundleview.setOnBundleClickListener(new BundleImgView.OnBundleSimpleClickListener() {
            @Override
            public void onPhotoAddClick(View v) {
                int count = MaxPhotoCount - bundleview.getResultCount();
                photoHelperEx.setMaxSelectCount(count);
                photoHelperEx.showDefaultDialog();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoHelperEx.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResult(String path) {
        String[] pathArray = PhotoHelper.format2Array(path);
        if (StrUtil.isEmpty(pathArray)) return;
        List<BundleImgEntity> pathList = new ArrayList<>();
        for (String str : pathArray) {
            pathList.add(new BundleImgEntity(str));
        }
        bundleview.addPhotos(pathList);
    }

    ////////////////////////


    public LocationInfo getLocation() {
        return location;
    }

    public List<String> getImgPaths() {
        List<String> imgpaths = new ArrayList<>();
        List<BundleImgEntity> results = bundleview.getResults();
        for (BundleImgEntity bean : results) {
            if (!bean.isVideo()) {
                imgpaths.add(bean.getPath());
            }
        }
        return imgpaths;
    }

    public String getVideoPath() {
        List<BundleImgEntity> results = bundleview.getResults();
        for (BundleImgEntity bean : results) {
            if (bean.isVideo()) {
                return bean.getPath();
            }
        }
        return "";
    }
}
