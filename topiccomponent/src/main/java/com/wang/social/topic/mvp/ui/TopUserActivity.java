package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.barview.BarUser;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.RouteNode;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicComponent;
import com.wang.social.topic.di.module.TopicModule;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.presenter.TopicPresenter;
import com.wang.social.topic.mvp.ui.adapter.TopUserAdapter;
import com.wang.social.topic.mvp.ui.fragment.TopUserFragment;

import java.util.List;

import butterknife.BindView;

@RouteNode(path = "/top_user_list", desc = "知识魔列表")
public class TopUserActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, TopUserActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_top_user;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.root_view, new TopUserFragment())
                .commit();
    }
}
