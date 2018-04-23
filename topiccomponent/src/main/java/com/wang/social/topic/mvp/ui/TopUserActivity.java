package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.mvp.ui.adapter.TopUserAdapter;

import butterknife.BindView;

public class TopUserActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, TopUserActivity.class);
        context.startActivity(intent);
    }

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_top_user;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mRecyclerView.setAdapter(new TopUserAdapter(mRecyclerView));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
