package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.entities.user.UserBoard;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.imageloader.ImageLoader;
import com.frame.mvp.IView;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;
import com.wang.social.personal.net.helper.NetUserHelper;

import javax.inject.Inject;

import butterknife.BindView;

public class QrcodeActivity extends BasicAppActivity implements IView {

    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_detail)
    TextView textDetail;
    @BindView(R2.id.img_qrcode)
    ImageView imgQrcode;
    @BindView(R2.id.text_lable_gender)
    TextView textLableGender;
    @BindView(R2.id.text_lable_astro)
    TextView textLableAstro;

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

    private void setUserData(UserBoard user) {
        if (user != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader,user.getAvatar());
            ImageLoaderHelper.loadImg(imgQrcode,user.getQrcodeImg());
            textName.setText(user.getNickname());
            textDetail.setText(user.getTagTextDot());
            textLableGender.setSelected(!user.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(user.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(user.getBirthday()));
        }
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_qs) {
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

    public void netGetUserInfo(int userId) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(CommonService.class).getUserInfoAndPhotos(userId),
                new ErrorHandleSubscriber<BaseJson<UserBoard>>() {
                    @Override
                    public void onNext(BaseJson<UserBoard> basejson) {
                        UserBoard user = basejson.getData();
                        setUserData(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
