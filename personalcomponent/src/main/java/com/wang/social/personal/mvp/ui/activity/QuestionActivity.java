package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.frame.base.BasicActivity;
import com.frame.component.common.AppConstant;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;

public class QuestionActivity extends BasicAppActivity {

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
        int i = v.getId();
        if (i == R.id.btn_qs) {
            WebActivity.start(this, AppConstant.Url.aboutQushai);

        } else if (i == R.id.btn_ql) {
            WebActivity.start(this, AppConstant.Url.aboutFanliao);

        } else if (i == R.id.btn_account) {
            WebActivity.start(this, AppConstant.Url.aboutAccount);

        } else if (i == R.id.btn_other) {
            WebActivity.start(this, AppConstant.Url.aboutOther);

        } else if (i == R.id.btn_version) {
            VersionHistoryActivity.start(this);

        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
