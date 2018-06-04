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
import com.wang.social.location.mvp.helper.LocationHelper;
import com.wang.social.location.mvp.model.entities.LocationInfo;
import com.wang.social.pictureselector.helper.PhotoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 逻辑整理，资源选择逻辑比较丰富：
 * 音频，视频，图片 三者相互不共存，具体逻辑如下：
 * 1：已有视频资源，则：音频选择禁用√，拍摄/录像按钮消失√
 * 2：已有图片资源，则：相机界面禁用录像功能√
 * 3：已有音频资源，则：相机界面禁用录像功能√
 */
public class FunshowAddBundleController extends FunshowAddBaseController implements PhotoHelper.OnPhotoCallback {

    @BindView(R2.id.bundleview)
    BundleImgView bundleview;
    @BindView(R2.id.text_position)
    TextView textPosition;

    private VideoPhotoHelperEx photoHelperEx;
    private int MaxPhotoCount = 9;

    private LocationHelper locationHelper;
    private LocationInfo location;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_LOCATION_SELECT:
                location = (LocationInfo) event.get("location");
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
                //如果已经存在音频资源 || 已经存在图片资源 ，则 不能录制视频
                if (getMusicBoardController().hasMusicRsc() || hasImageRsc()) {
                    photoHelperEx.setVideoEnable(false);
                } else {
                    photoHelperEx.setVideoEnable(true);
                }
                photoHelperEx.showDefaultDialog();
            }
        });
    }

    @Override
    protected void onInitData() {
        //开始定位
        locationHelper = LocationHelper.newInstance().startLocation();
        locationHelper.setOnLocationListener(locationInfo -> {
            if (locationInfo != null) {
                location = locationInfo;
                textPosition.setText(location.getProvince() + location.getCity() + location.getAddress());
            } else {
                textPosition.setText("定位失败");
            }
        });
    }

    @Override
    public void onDestory() {
        super.onDestory();
        if (locationHelper != null) locationHelper.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoHelperEx.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResult(String path) {
        //获取图片或视频资源，为空不处理
        String[] pathArray = PhotoHelper.format2Array(path);
        if (StrUtil.isEmpty(pathArray)) return;
        //转换为bundle集合
        List<BundleImgEntity> pathList = new ArrayList<>();
        for (String str : pathArray) {
            pathList.add(new BundleImgEntity(str));
        }
        //如果是视频资源则限定最大数量为1，否则9
        if (pathList.get(0).isVideo()) {
            bundleview.setMaxcount(1);
        } else {
            bundleview.setMaxcount(9);
        }
        //设置数据刷新
        bundleview.addPhotos(pathList);
    }

    /////////////////// 对外方法 /////////////////////

    //获取位置信息，没有返回null
    public LocationInfo getLocation() {
        return location;
    }

    //检查是否已经包含视频资源
    public boolean hasVideoRsc() {
        List<BundleImgEntity> results = bundleview.getResults();
        for (BundleImgEntity bean : results) {
            if (bean.isVideo()) {
                return true;
            }
        }
        return false;
    }

    //检查是否已经包含图片资源
    public boolean hasImageRsc() {
        List<BundleImgEntity> results = bundleview.getResults();
        for (BundleImgEntity bean : results) {
            if (!bean.isVideo()) {
                return true;
            }
        }
        return false;
    }

    //获取图片资源集合，没有返回0size集合
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

    //获取视频资源路径，没有返回空字符串
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
