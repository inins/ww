package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.R;

public class AboutActivity extends WebActivity {

    public static void start(Context context) {
        start(context, "https://www.bing.com");
    }

    public static void start(Context context, String url) {
        start(context, "", url);
    }

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        TextView btn_right = getRightBtn();
        btn_right.setText(getResources().getString(R.string.personal_about_btn_right));
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionActivity.start(AboutActivity.this);
            }
        });
    }
}
