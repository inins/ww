package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterAiteUserList;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterTest;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterTest2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestActivity extends AppCompatActivity {

    SpringView springView;
    RecyclerView recycler;
    Toolbar toolbar;
    private RecycleAdapterTest2 adapter;


    public static void start(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funshow_activity_test);

        initData();
    }

    public void initData() {
        recycler = findViewById(R.id.recycler);
        springView = findViewById(R.id.spring);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
//        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterTest2();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                springView.onFinishFreshAndLoad();
                            }
                        },100);

                        adapter.getData().add(new TestEntity());
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 1000);
            }
        });

        //测试数据
        List<TestEntity> results = new ArrayList<TestEntity>() {{
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
        }};
        adapter.getData().clear();
        adapter.getData().addAll(results);
        adapter.notifyDataSetChanged();
    }
}
