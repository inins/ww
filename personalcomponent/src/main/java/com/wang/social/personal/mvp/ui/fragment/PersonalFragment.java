package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.frame.base.BasicFragment;
import com.frame.http.api.ApiHelper;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.base.BaseFragment;
import com.frame.component.path.LoginPath;
import com.frame.component.router.ui.UIRouter;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.di.component.DaggerFragmentComponent;
import com.wang.social.personal.di.component.FragmentComponent;
import com.wang.social.personal.mvp.ui.activity.FeedbackActivity;
import com.wang.social.personal.mvp.ui.activity.MeDetailActivity;
import com.wang.social.personal.mvp.ui.activity.SettingActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ========================================
 * 个人中心
 * <p>
 * Create by ChenJing on 2018-03-23 16:22
 * ========================================
 */

public class PersonalFragment extends BasicFragment {

    @BindView(R2.id.header)
    ImageView header;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @Inject
    ImageLoader mImageLoader;

    public static PersonalFragment newInstance() {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_personal_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.bringToFront();
        mImageLoader.loadImage(mActivity, ImageConfigImpl.
                builder()
                .imageView(header)
                .url("http://resouce.dongdongwedding.com/2017-08-08_rtUbDxhH.png")
                .build());
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick({R2.id.header, R2.id.btn_right, R2.id.btn_me_account, R2.id.btn_me_lable, R2.id.btn_me_feedback, R2.id.btn_me_share, R2.id.btn_me_about, R2.id.btn_me_eva})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.header:
                MeDetailActivity.start(getContext());
                break;
            case R.id.btn_right:
                SettingActivity.start(getContext());
                break;
            case R.id.btn_me_account:
                break;
            case R.id.btn_me_lable:
                break;
            case R.id.btn_me_feedback:
                FeedbackActivity.start(getContext());
                break;
            case R.id.btn_me_share:
                break;
            case R.id.btn_me_about:
                break;
            case R.id.btn_me_eva:
                break;
        }
    }
//        UIRouter.getInstance().openUri(mActivity, LoginPath.LOGIN_URL, null);
}
