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
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.ItemDecorationDivider;
import com.wang.social.personal.mvp.entities.ShowListCate;
import com.wang.social.personal.mvp.entities.ShowListGroup;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterPrivacyShowList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PrivacyShowListActivity extends BasicAppActivity implements BaseAdapter.OnItemClickListener<ShowListCate> {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    private RecycleAdapterPrivacyShowList adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, PrivacyShowListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_showlist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterPrivacyShowList();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        //测试数据
        List<ShowListCate> results = new ArrayList() {{
            add(new ShowListCate("公开", new ArrayList() {{
                add(new ShowListGroup("群组名称一"));
                add(new ShowListGroup("群组名称二"));
                add(new ShowListGroup("群组名称三"));
                add(new ShowListGroup("群组名称四"));
            }}));
            add(new ShowListCate("仅自己可见", new ArrayList() {{
                add(new ShowListGroup("群组名称一"));
                add(new ShowListGroup("群组名称二"));
                add(new ShowListGroup("群组名称三"));
                add(new ShowListGroup("群组名称四"));
            }}));
            add(new ShowListCate("好友可见", new ArrayList() {{
                add(new ShowListGroup("群组名称一"));
                add(new ShowListGroup("群组名称二"));
                add(new ShowListGroup("群组名称三"));
                add(new ShowListGroup("群组名称四"));
            }}));
            add(new ShowListCate("趣聊/觅聊可见", new ArrayList() {{
                add(new ShowListGroup("群组名称一"));
                add(new ShowListGroup("群组名称二"));
                add(new ShowListGroup("群组名称三"));
                add(new ShowListGroup("群组名称四"));
            }}));
        }};
        adapter.refreshData(results);
    }

    public void onClick(View v) {

    }

    @Override
    public void onItemClick(ShowListCate testEntity, int position) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
