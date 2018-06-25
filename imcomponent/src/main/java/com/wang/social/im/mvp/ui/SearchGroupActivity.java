package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchFriend;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchGroup;

import java.util.ArrayList;

import butterknife.BindView;

public class SearchGroupActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.text_toolbar_title)
    TextView textToolbarTitle;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;

    private String key;
    private RecycleAdapterSearchGroup adapter;

    public static void start(Context context, String key) {
        Intent intent = new Intent(context, SearchGroupActivity.class);
        intent.putExtra("key", key);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_search_friend;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        key = getIntent().getStringExtra("key");
        textToolbarTitle.setText("搜索群组-" + key);

        adapter = new RecycleAdapterSearchGroup();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));
        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> springView.onFinishFreshAndLoadDelay(), 1000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(() -> springView.onFinishFreshAndLoadDelay(), 1000);
            }
        });

        adapter.refreshData(new ArrayList<TestEntity>() {{
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
        }});
    }
}
