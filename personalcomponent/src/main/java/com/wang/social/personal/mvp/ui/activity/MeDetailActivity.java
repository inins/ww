package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.base.BasicActivity;
import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.di.component.AppComponent;
import com.frame.di.component.DaggerAppComponent;
import com.frame.di.scope.ActivityScope;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.integration.IRepositoryManager;
import com.frame.integration.RepositoryManager;
import com.wang.social.personal.R;
import com.wang.social.personal.data.db.AddressDataBaseManager;
import com.wang.social.personal.di.component.DaggerActivityComponent;
import com.wang.social.personal.di.module.MeDetailModule;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.presonter.MeDetailPresonter;
import com.wang.social.personal.mvp.ui.dialog.DialogAddressPicker;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomGender;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomPhoto;
import com.wang.social.personal.mvp.ui.dialog.DialogDatePicker;
import com.wang.social.personal.mvp.ui.dialog.DialogInput;
import com.wang.social.personal.mvp.ui.dialog.DialogSure;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Component;
import timber.log.Timber;

public class MeDetailActivity extends BaseAppActivity<MeDetailPresonter> implements MeDetailContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_gender)
    TextView textGender;
    @BindView(R.id.text_old)
    TextView textOld;
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.text_photo)
    TextView textPhoto;
    @BindView(R.id.text_sign)
    TextView textSign;
    private DialogBottomGender dialogGender;
    private DialogBottomPhoto dialogphoto;
    private DialogAddressPicker dialogAddress;
    private DialogDatePicker dialogDate;
    private DialogInput dialogInputName;
    private DialogInput dialogInputSign;
    @Inject
    ImageLoader mImageLoader;

    public static void start(Context context) {
        Intent intent = new Intent(context, MeDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_medetail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        dialogInputName = DialogInput.newDialogName(this);
        dialogInputSign = DialogInput.newDialogSign(this);
        dialogAddress = new DialogAddressPicker(this);
        dialogphoto = new DialogBottomPhoto(this);
        dialogGender = new DialogBottomGender(this);
        dialogDate = new DialogDatePicker(this);
        dialogInputName.setOnInputListener(text -> {
            textName.setText(text);
            mPresenter.updateUserName(text);
        });
        dialogInputSign.setOnInputListener(text -> {
            textSign.setText(text);
            mPresenter.updateUserSign(text);
        });
        dialogAddress.setOnAddressSelectListener((province, city) -> {
            textAddress.setText(province + city);
            mPresenter.updateUserAddress(province, city);
        });
        dialogDate.setOnDateChooseListener((year, month, day, astro, showText) -> {
            textOld.setText(showText);
            mPresenter.updateUserBirthday(year + "-" + month + "-" + day);
        });
        dialogGender.setOnGenderSelectListener(gender -> {
            textGender.setText(gender);
            dialogGender.dismiss();
            mPresenter.updateUserGender(gender);
        });

        setUserData();
    }

    public void setUserData() {
        User user = AppDataHelper.getUser();
        if (user != null) {
            mImageLoader.loadImage(this, ImageConfigImpl.
                    builder()
                    .imageView(imgHeader)
                    .url(user.getAvatar())
                    .build());
            textName.setText(user.getNickname());
            textGender.setText(user.getSexText());
            textOld.setText(user.getBirthday());
            textAddress.setText(user.getProvince() + user.getCity());
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_header:
                new DialogSure(this, "测试消息提示").show();
                mPresenter.uploadImg("xxxxx");
                break;
            case R.id.lay_name:
                dialogInputName.setText(textName.getText().toString());
                dialogInputName.show();
                break;
            case R.id.lay_gender:
                dialogGender.show();
                break;
            case R.id.lay_old:
                dialogDate.setDate(textOld.getText().toString());
                dialogDate.show();
                break;
            case R.id.lay_address:
                dialogAddress.show();
                break;
            case R.id.lay_photo:
                dialogphoto.show();
                break;
            case R.id.lay_sign:
                dialogInputSign.setText(textSign.getText().toString());
                dialogInputSign.show();
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .meDetailModule(new MeDetailModule(this))
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

    @Override
    public void finishActivity() {
        Timber.e("finishActivity");
    }
}
