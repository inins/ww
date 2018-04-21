package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.TitleView;
import com.frame.component.view.bundleimgview.BundleImgEntity;
import com.frame.component.view.bundleimgview.BundleImgView;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterHotUserList;
import com.wang.social.funshow.mvp.ui.dialog.MusicPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FunshowAddActivity extends BasicAppActivity implements BaseAdapter.OnItemClickListener<TestEntity> {

    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.bundleview)
    BundleImgView bundleview;

    public static void start(Context context) {
        Intent intent = new Intent(context, FunshowAddActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_funshowadd;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        bundleview.setMaxcount(9);
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

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
        } else if (i == R.id.btn_aite) {
            AiteUserListActivity.start(this);
        } else if (i == R.id.btn_position) {

        } else if (i == R.id.btn_lock) {
            LockActivity.start(this);
        } else if (i == R.id.btn_music) {
            new MusicPopupWindow(this).showPopupWindow(v);
        } else if (i == R.id.btn_keyboard) {

        }
    }

    @Override
    public void onItemClick(TestEntity testEntity, int position) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
