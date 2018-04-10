package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.mvp.IView;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;
import com.wang.social.personal.net.helper.NetUserHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrcodeActivity extends BasicAppActivity implements IView {

    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_detail)
    TextView textDetail;
    @BindView(R.id.img_qrcode)
    ImageView imgQrcode;

    @Inject
    ImageLoader mImageLoader;
    @Inject
    NetUserHelper netUserHelper;

    private int userId;

    public static void start(Context context, int userId) {
        Intent intent = new Intent(context, QrcodeActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_qrcode;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        userId = getIntent().getIntExtra("userId", 0);
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextLight(this);

        netGetUserInfo(userId);
    }

    private void setUserData(QrcodeInfo qrcodeInfo) {
        if (qrcodeInfo != null) {
            mImageLoader.loadImage(this, ImageConfigImpl.builder()
                    .imageView(imgHeader)
                    .isCircle(true)
                    .url(qrcodeInfo.getUserAvatar())
                    .build());
            mImageLoader.loadImage(this, ImageConfigImpl.builder()
                    .imageView(imgQrcode)
                    .url(qrcodeInfo.getQrcodeImg())
                    .build());
            textName.setText(qrcodeInfo.getNickName());

        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qs:
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    /////////////////////////////////

    private void netGetUserInfo(int userId) {
        netUserHelper.getUserInfoByUserId(this, userId, new NetUserHelper.OnUserApiCallBack() {
            @Override
            public void onSuccess(QrcodeInfo qrcodeInfo) {
                setUserData(qrcodeInfo);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToastLong(e.getMessage());
            }
        });
    }
}
