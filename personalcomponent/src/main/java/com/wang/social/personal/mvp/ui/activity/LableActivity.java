package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.common.GridSpacingItemDecoration;
import com.wang.social.personal.common.ItemDecorationDivider;
import com.wang.social.personal.mvp.entities.Lable;
import com.wang.social.personal.mvp.entities.TestEntity;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterDepositDetail;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterLable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LableActivity extends BasicActivity implements BaseAdapter.OnItemClickListener<Lable> {

    @BindView(R2.id.recycler_show)
    RecyclerView recycler_show;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.btn_right)
    TextView btn_right;
    private RecycleAdapterLable adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, LableActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_lable;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterLable();
        adapter.setOnItemClickListener(this);
        recycler_show.setNestedScrollingEnabled(false);
        recycler_show.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_show.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));
        recycler_show.setAdapter(adapter);

        //测试数据
        List<Lable> results = new ArrayList() {{
            add(new Lable("多肉植物", true));
            add(new Lable("狗狗"));
            add(new Lable("火锅"));
            add(new Lable("唱歌"));
        }};
        adapter.refreshData(results);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                if (!adapter.isDeleteEnable()) {
                    btn_right.setText(getResources().getString(R.string.personal_lable_btn_right_finish));
                    adapter.setDeleteEnable(true);
                    adapter.notifyDataSetChanged();
                } else {
                    btn_right.setText(getResources().getString(R.string.personal_lable_btn_right_edit));
                    adapter.setDeleteEnable(false);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onItemClick(Lable testEntity, int position) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
