package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.base.BaseAdapter;
import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.common.GridSpacingItemDecoration;
import com.wang.social.personal.mvp.entities.Lable;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterLable;
import com.wang.social.personal.mvp.ui.dialog.DialogFragmentLable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LableActivity extends BasicActivity implements BaseAdapter.OnItemClickListener<Lable> {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.btn_right)
    TextView btn_right;
    @BindView(R2.id.recycler_show)
    RecyclerView recycler_show;
    @BindView(R2.id.recycler_me)
    RecyclerView recycler_me;
    @BindView(R2.id.tablayout)
    TabLayout tablayout;

    private RecycleAdapterLable adapter_show;
    private RecycleAdapterLable adapter_me;

    private DialogFragmentLable dialogLable;

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
        setSupportActionBar(toolbar);
        dialogLable = new DialogFragmentLable();

        adapter_show = new RecycleAdapterLable();
        adapter_show.setOnItemClickListener(this);
        recycler_show.setNestedScrollingEnabled(false);
        recycler_show.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_show.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));
        recycler_show.setAdapter(adapter_show);

        adapter_me = new RecycleAdapterLable();
        recycler_me.setNestedScrollingEnabled(false);
        recycler_me.setLayoutManager(ChipsLayoutManager.newBuilder(this).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        recycler_me.addItemDecoration(new SpacingItemDecoration(SizeUtils.dp2px(5), SizeUtils.dp2px(5)));
        recycler_me.setAdapter(adapter_me);

        tablayout.addTab(tablayout.newTab().setText("大家都在用"));
        tablayout.addTab(tablayout.newTab().setText("爱好"));
        tablayout.addTab(tablayout.newTab().setText("社交"));
        tablayout.addTab(tablayout.newTab().setText("职场"));
        tablayout.addTab(tablayout.newTab().setText("奇思妙想"));

        //测试数据
        List<Lable> results = new ArrayList() {{
            add(new Lable("多肉植物", true));
            add(new Lable("狗狗"));
            add(new Lable("火锅"));
            add(new Lable("唱歌"));
        }};
        adapter_show.refreshData(results);
        List<Lable> results_me = new ArrayList() {{
            add(new Lable("成都麻将", true));
            add(new Lable("互联网"));
            add(new Lable("中国好声音"));
            add(new Lable("健身"));
            add(new Lable("徒步旅行"));
            add(new Lable("多肉植物", true));
            add(new Lable("狗狗"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
            add(new Lable("多肉植物", true));
            add(new Lable("狗狗"));
            add(new Lable("徒步旅行"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
            add(new Lable("多肉植物", true));
            add(new Lable("狗狗"));
            add(new Lable("徒步旅行"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
            add(new Lable("多肉植物", true));
            add(new Lable("狗狗"));
            add(new Lable("徒步旅行"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
            add(new Lable("唱歌"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
            add(new Lable("多肉植物", true));
            add(new Lable("狗狗"));
            add(new Lable("徒步旅行"));
            add(new Lable("火锅"));
            add(new Lable("中国好声音"));
            add(new Lable("唱歌"));
        }};
        adapter_me.refreshData(results_me);
    }

    @Override
    public void onItemClick(Lable lable, int position) {
        dialogLable.show(getSupportFragmentManager(), "dialogFragmentLable");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                if (!adapter_show.isDeleteEnable()) {
                    btn_right.setText(getResources().getString(R.string.personal_lable_btn_right_finish));
                    adapter_show.setDeleteEnable(true);
                    adapter_show.notifyDataSetChanged();
                    adapter_me.setDeleteEnable(true);
                    adapter_me.notifyDataSetChanged();
                } else {
                    btn_right.setText(getResources().getString(R.string.personal_lable_btn_right_edit));
                    adapter_show.setDeleteEnable(false);
                    adapter_show.notifyDataSetChanged();
                    adapter_me.setDeleteEnable(false);
                    adapter_me.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
