package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.common.ItemDecorationDivider;
import com.wang.social.personal.mvp.entities.TestEntity;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterDepositRecord;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AccountDepositRecordActivity extends BasicActivity implements BaseAdapter.OnItemClickListener<TestEntity> {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private RecycleAdapterDepositRecord adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AccountDepositRecordActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_account_deposit_record;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterDepositRecord();
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
        }};
        adapter.refreshData(results);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                break;
        }
    }

    @Override
    public void onItemClick(TestEntity testEntity, int position) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}