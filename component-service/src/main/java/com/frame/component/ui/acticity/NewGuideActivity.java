package com.frame.component.ui.acticity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.frame.component.service.R;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.utils.StatusBarUtil;

public class NewGuideActivity extends BasicAppNoDiActivity {


    public static void start(Context context) {
        Intent intent = new Intent(context, NewGuideActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_newguide;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextLight(this);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_go) {
            NewGuideRecommendActivity.start(this);

        }
    }
}
