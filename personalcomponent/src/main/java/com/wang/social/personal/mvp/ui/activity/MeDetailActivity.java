package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.RegexUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.R2;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.personal.R;
import com.wang.social.personal.data.db.AddressDataBaseManager;
import com.wang.social.personal.di.component.DaggerActivityComponent;
import com.wang.social.personal.di.module.MeDetailModule;
import com.wang.social.personal.helper.PicPhotoHelperEx;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;
import com.wang.social.personal.mvp.presonter.MeDetailPresonter;
import com.wang.social.personal.mvp.ui.dialog.DialogAddressPicker;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomGender;
import com.wang.social.personal.mvp.ui.dialog.DialogDatePicker;
import com.frame.component.ui.dialog.DialogInput;

import javax.inject.Inject;

import butterknife.BindView;

public class MeDetailActivity extends BaseAppActivity<MeDetailPresonter> implements MeDetailContract.View, PhotoHelper.OnPhotoCallback {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_gender)
    TextView textGender;
    @BindView(R2.id.text_old)
    TextView textOld;
    @BindView(R2.id.text_address)
    TextView textAddress;
    @BindView(R2.id.text_photo)
    TextView textPhoto;
    @BindView(R2.id.text_sign)
    TextView textSign;
    private DialogBottomGender dialogGender;
    private DialogAddressPicker dialogAddress;
    private DialogDatePicker dialogDate;
    private DialogInput dialogInputName;
    private DialogInput dialogInputSign;
    private PicPhotoHelperEx photoHelper;
    @Inject
    ImageLoader mImageLoader;

    public static void start(Context context) {
        if (CommonHelper.LoginHelper.isLogin()) {
            Intent intent = new Intent(context, MeDetailActivity.class);
            context.startActivity(intent);
        } else {
            CommonHelper.LoginHelper.startLoginActivity(context);
        }
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_medetail;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_MEPHOTO_CHANGE:
                mPresenter.getPhotoList();
                break;
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        photoHelper = PicPhotoHelperEx.newInstance(this, this).needOfficialPhoto(true);
        dialogInputName = DialogInput.newDialogName(this);
        dialogInputSign = DialogInput.newDialogSign(this);
        dialogAddress = new DialogAddressPicker(this);
        dialogGender = new DialogBottomGender(this);
        dialogDate = new DialogDatePicker(this);
        dialogInputName.setOnInputListener(text -> {
            if (TextUtils.isEmpty(text)) {
                dialogInputName.dismiss();
                return;
            }
            if (RegexUtils.isUsernameMe(text)) {
                textName.setText(text);
                mPresenter.updateUserName(text);
                dialogInputName.dismiss();
            } else {
                ToastUtil.showToastLong("仅允许输入下划线符号");
            }
        });
        dialogInputSign.setOnInputListener(text -> {
            if (!TextUtils.isEmpty(text)) {
                textSign.setText(text);
                mPresenter.updateUserSign(text);
            }
            dialogInputSign.dismiss();
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
        mPresenter.getPhotoList();
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
            textOld.setText(DialogDatePicker.fixDateStr(user.getBirthday()));
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

    @Override
    public void setPhotoCount(int count) {
        textPhoto.setText(count != 0 ? (count + "张照片") : "");
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lay_header) {
            photoHelper.showDefaultDialog();

        } else if (i == R.id.lay_name) {
            dialogInputName.setText(textName.getText().toString());
            dialogInputName.show();

        } else if (i == R.id.lay_gender) {
            dialogGender.show();

        } else if (i == R.id.lay_old) {
            dialogDate.setDate(textOld.getText().toString());
            dialogDate.show();

        } else if (i == R.id.lay_address) {
            dialogAddress.show();

        } else if (i == R.id.lay_photo) {
            MePhotoActivity.start(this);

        } else if (i == R.id.lay_sign) {
            dialogInputSign.setText(textSign.getText().toString());
            dialogInputSign.show();

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
