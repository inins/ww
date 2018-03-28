package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.BarUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.mvp.ui.fragment.PersonalFragment;

public class MeDetailActivity extends BasicActivity {

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

    }

    public void onClick(View v) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
