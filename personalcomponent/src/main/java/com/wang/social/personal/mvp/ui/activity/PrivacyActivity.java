package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;

public class PrivacyActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, PrivacyActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_privacy;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_friend) {
            PrivacyShowListActivity.start(this);

        } else if (i == R.id.btn_ql) {
            PrivacyShowListActivity.start(this);

        } else if (i == R.id.btn_qs) {
            PrivacyShowListActivity.start(this);

        } else if (i == R.id.btn_topic) {
            PrivacyShowListActivity.start(this);

        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
