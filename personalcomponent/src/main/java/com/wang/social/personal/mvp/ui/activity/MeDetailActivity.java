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

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.di.component.DaggerAppComponent;
import com.frame.di.scope.ActivityScope;
import com.frame.integration.IRepositoryManager;
import com.frame.integration.RepositoryManager;
import com.wang.social.personal.R;
import com.wang.social.personal.data.db.AddressDataBaseManager;
import com.wang.social.personal.di.component.DaggerActivityComponent;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;
import com.wang.social.personal.mvp.model.api.UserService;
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

@ActivityScope
public class MeDetailActivity extends BasicActivity {

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
        dialogInputName.setOnInputListener(text -> textName.setText(text));
        dialogInputSign.setOnInputListener(text -> textSign.setText(text));
        dialogAddress.setOnAddressSelectListener((province, city) -> textAddress.setText(province + city));
        dialogDate.setOnDateChooseListener((year, month, day, astro, showText) -> textOld.setText(showText));
        dialogGender.setOnGenderSelectListener(gender -> {
            textGender.setText(gender);
            dialogGender.dismiss();
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_header:
                new DialogSure(this, "测试消息提示").show();
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
                mRepositoryManager.obtainRetrofitService(UserService.class).getQiniuToken();
                break;
            case R.id.lay_sign:
                dialogInputSign.setText(textSign.getText().toString());
                dialogInputSign.show();
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        Log.e("test","setupActivityComponent");
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Inject
    IRepositoryManager mRepositoryManager;
}
