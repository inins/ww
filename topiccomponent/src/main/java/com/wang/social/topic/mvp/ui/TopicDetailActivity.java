package com.wang.social.topic.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.topic.R;

public class TopicDetailActivity extends BasicActivity {
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_topic_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }
}
