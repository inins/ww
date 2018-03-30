package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomGender;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomPhoto;
import com.wang.social.personal.mvp.ui.dialog.DialogInput;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        dialogphoto = new DialogBottomPhoto(this);
        dialogGender = new DialogBottomGender(this);
        dialogGender.setOnGenderSelectListener(new DialogBottomGender.OnGenderSelectListener() {
            @Override
            public void onGenderSelect(String gender) {
                textGender.setText(gender);
                dialogGender.dismiss();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_header:
                break;
            case R.id.lay_name:
                new DialogInput(this).show();
                break;
            case R.id.lay_gender:
                dialogGender.show();
                break;
            case R.id.lay_old:
                break;
            case R.id.lay_address:
                break;
            case R.id.lay_photo:
                dialogphoto.show();
                break;
            case R.id.lay_sign:
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
