package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
        mImageLoader.loadImage(mActivity, ImageConfigImpl.
                builder()
                .imageView(header)
                .url("http://resouce.dongdongwedding.com/2017-08-08_rtUbDxhH.png")
                .build());
    }

    @Override
    public void setData(@Nullable Object data) {

    }

//    @OnClick(R2.id.goto_login)
//    public void onViewClicked() {
//        UIRouter.getInstance().openUri(mActivity, LoginPath.LOGIN_URL, null);
//    }
}
