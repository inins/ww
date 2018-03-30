package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.frame.base.BasicActivity;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;

public class QuestionActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, QuestionActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_question;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qs:
                WebActivity.start(this);
                break;
            case R.id.btn_ql:
                WebActivity.start(this);
                break;
            case R.id.btn_account:
                WebActivity.start(this);
                break;
            case R.id.btn_other:
                WebActivity.start(this);
                break;
            case R.id.btn_version:
                VersionHistoryActivity.start(this);
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
