package com.wang.social.funpoint.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.common.SimpleTextWatcher;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.LoadingLayout;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.di.component.DaggerSingleActivityComponent;
import com.wang.social.funpoint.helper.SpringViewHelper;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterSearch;
import com.wang.social.funpoint.mvp.ui.view.ConerEditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BasicAppActivity implements BaseAdapter.OnItemClickListener<TestEntity> {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_right)
    TextView btnRight;
    @BindView(R2.id.spring)
    SpringView spring;
    @BindView(R2.id.loadingview)
    LoadingLayout loadingview;
    @BindView(R2.id.edit_search)
    EditText editSearch;

//    @Inject
//    ImageLoaderHelper imageLoaderHelper;

    private RecycleAdapterSearch adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funpoint_activity_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterSearch();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));
        SpringViewHelper.initSpringViewForTest(spring);
        loadingview.showLackView();
        editSearch.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                String key = editSearch.getText().toString();
                if (!TextUtils.isEmpty(key)) {
                    loadingview.showOut();
                } else {
                    loadingview.showLackView();
                }
                return true;
            }
            return false;
        });

        //测试数据
        List<TestEntity> results = new ArrayList() {{
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
        int id = v.getId();
        if (id == R.id.btn_right) {
            finish();
        }
    }

    @Override
    public void onItemClick(TestEntity testEntity, int position) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }
}
