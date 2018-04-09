package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.helper.PhotoHelper;
import com.wang.social.personal.R;
import com.wang.social.personal.data.db.AddressDataBaseManager;
import com.wang.social.personal.di.component.DaggerActivityComponent;
import com.wang.social.personal.di.module.MeDetailModule;
import com.wang.social.personal.helper.PhotoHelperEx;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;
import com.wang.social.personal.mvp.presonter.MeDetailPresonter;
import com.wang.social.personal.mvp.ui.dialog.DialogAddressPicker;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomGender;
import com.wang.social.personal.mvp.ui.dialog.DialogDatePicker;
import com.wang.social.personal.mvp.ui.dialog.DialogInput;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

public class MeDetailActivity extends BaseAppActivity<MeDetailPresonter> implements MeDetailContract.View, PhotoHelper.OnPhotoCallback {

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
    private DialogAddressPicker dialogAddress;
    private DialogDatePicker dialogDate;
    private DialogInput dialogInputName;
    private DialogInput dialogInputSign;
    private PhotoHelperEx photoHelper;
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
        photoHelper = PhotoHelperEx.newInstance(this, this).needOfficialPhoto(true);
        dialogInputName = DialogInput.newDialogName(this);
        dialogInputSign = DialogInput.newDialogSign(this);
        dialogAddress = new DialogAddressPicker(this);
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
            textAddress.setText(province.getName() + (city != null ? city.getName() : ""));
            mPresenter.updateUserAddress(province.getId() + "", city != null ? city.getId() + "" : null);
        });
        dialogDate.setOnDateChooseListener((year, month, day, astro, showDate) -> {
            textOld.setText(showDate + " " + astro);
            mPresenter.updateUserBirthday(showDate);
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
            mImageLoader.loadImage(this, ImageConfigImpl.builder()
                    .imageView(imgHeader)
                    .isCircle(true)
                    .url(user.getAvatar())
                    .build());
            textName.setText(user.getNickname());
            textGender.setText(user.getSexText());
            textOld.setText(user.getBirthday());
            Province province = AddressDataBaseManager.getInstance().queryProvinceById(user.getProvinceInt());
            City city = AddressDataBaseManager.getInstance().queryCityById(user.getCityInt());
            textAddress.setText((province != null ? province.getName() : "") + (city != null ? city.getName() : ""));
            textSign.setText(user.getAutograph());
        }
    }

    @Override
    public void setHeaderImg(String url) {
        mImageLoader.loadImage(this, ImageConfigImpl.builder()
                .imageView(imgHeader)
                .url(url)
                .isCircle(true)
                .build());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_header:
                photoHelper.showDefaultDialog();
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
                MePhotoActivity.start(this);
                break;
            case R.id.lay_sign:
                dialogInputSign.setText(textSign.getText().toString());
                dialogInputSign.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResult(String path) {
        mPresenter.updateUserAvatar(path);
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
}
