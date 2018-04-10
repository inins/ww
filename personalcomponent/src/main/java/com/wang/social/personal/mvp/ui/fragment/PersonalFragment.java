package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BasicFragment;
import com.frame.component.common.AppConstant;
import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.data.db.AddressDataBaseManager;
import com.wang.social.personal.di.component.DaggerFragmentComponent;
import com.wang.social.personal.di.module.UserModule;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;
import com.wang.social.personal.mvp.ui.activity.AboutActivity;
import com.wang.social.personal.mvp.ui.activity.AccountActivity;
import com.wang.social.personal.mvp.ui.activity.FeedbackActivity;
import com.wang.social.personal.mvp.ui.activity.LableActivity;
import com.wang.social.personal.mvp.ui.activity.MeDetailActivity;
import com.wang.social.personal.mvp.ui.activity.QrcodeActivity;
import com.wang.social.personal.mvp.ui.activity.SettingActivity;
import com.wang.social.personal.net.helper.NetUserHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * ========================================
 * 个人中心
 * <p>
 * Create by ChenJing on 2018-03-23 16:22
 * ========================================
 */

public class PersonalFragment extends BasicFragment {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_left)
    ImageView btnLeft;
    @BindView(R.id.btn_right)
    ImageView btnRight;
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_count_sq)
    TextView textCountSq;
    @BindView(R.id.text_count_ht)
    TextView textCountHt;

    @Inject
    ImageLoader mImageLoader;
    @Inject
    NetUserHelper netUserHelper;

    Unbinder unbinder;

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
                .userModule(new UserModule())
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
        setUserData();
    }

    private void setUserData() {
        User user = AppDataHelper.getUser();
        if (user != null) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.
                    builder()
                    .imageView(imgHeader)
                    .isCircle(true)
                    .url(user.getAvatar())
                    .build());
            textName.setText(user.getNickname());
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick({R2.id.lay_nameboard, R2.id.btn_right, R2.id.btn_left, R2.id.btn_me_account, R2.id.btn_me_lable, R2.id.btn_me_feedback, R2.id.btn_me_share, R2.id.btn_me_about, R2.id.btn_me_eva})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.lay_nameboard:
                AddressDataBaseManager.init();
                MeDetailActivity.start(getContext());
                break;
            case R.id.btn_right:
                SettingActivity.start(getContext());
                break;
            case R.id.btn_left:
                User user = AppDataHelper.getUser();
                if (user != null) {
                    QrcodeActivity.start(getContext(), user.getUserId());
                }
                break;
            case R.id.btn_me_account:
                AccountActivity.start(getContext());
                break;
            case R.id.btn_me_lable:
                LableActivity.start(getContext());
                break;
            case R.id.btn_me_feedback:
                FeedbackActivity.start(getContext());
                break;
            case R.id.btn_me_share:
                break;
            case R.id.btn_me_about:
                AboutActivity.start(getContext(), AppConstant.Url.wwAbout);
                break;
            case R.id.btn_me_eva:
                netUserHelper.loginTest();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
