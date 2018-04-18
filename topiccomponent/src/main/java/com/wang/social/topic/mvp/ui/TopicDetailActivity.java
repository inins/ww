package com.wang.social.topic.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.base.BasicActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.topic.R;

import butterknife.BindView;

public class TopicDetailActivity extends BasicActivity {

    @BindView(R.id.toolbar)
    SocialToolbar mToolbar;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_topic_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
//        mToolbar.setTitle("纪录片意外走红B站：冷门纪录片要“征服”资本？纪录片意外走红B站：冷门纪录片要“征服”资本？");
    }
}
