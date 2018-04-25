package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.helper.SpringViewHelper;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterAiteUserList;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterHotUserList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AiteUserListActivity extends BasicAppActivity {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    private RecycleAdapterAiteUserList adapter;


    public static void start(Context context) {
        Intent intent = new Intent(context, AiteUserListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_aiteuserlist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterAiteUserList();
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
                        adapter.getData().add(new TestEntity());
                        adapter.notifyDataSetChanged();
                        springView.onFinishFreshAndLoad();
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
        }};
        adapter.refreshData(results);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
