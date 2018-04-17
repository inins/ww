package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.ItemDecorationDivider;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterBlacklist;
import com.wang.social.personal.mvp.ui.view.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlackListActivity extends BasicAppActivity implements BaseAdapter.OnItemClickListener<TestEntity> {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.titleview)
    TitleView titleview;
    private RecycleAdapterBlacklist adapter;

    private boolean isBlankList;

    public static void startBlankList(Context context) {
        start(context, true);
    }

    public static void startShutdownList(Context context) {
        start(context, false);
    }

    private static void start(Context context, boolean isBlankList) {
        Intent intent = new Intent(context, BlackListActivity.class);
        intent.putExtra("isBlankList", isBlankList);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_blacklist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        isBlankList = getIntent().getBooleanExtra("isBlankList", false);
        titleview.setTitle(getResources().getString(isBlankList ? R.string.personal_blacklist_title : R.string.personal_shutdown_title));
        titleview.setNote(getResources().getString(isBlankList ? R.string.personal_blacklist_title_note : R.string.personal_shutdown_title_note));

        adapter = new RecycleAdapterBlacklist();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        //测试数据
        List<TestEntity> results = new ArrayList() {{
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
    public void onItemClick(TestEntity testEntity, int position) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
