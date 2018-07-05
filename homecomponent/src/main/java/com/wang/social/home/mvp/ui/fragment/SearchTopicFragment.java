package com.wang.social.home.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.di.component.AppComponent;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterSearchFunshow;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterSearchTopic;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class SearchTopicFragment extends BasicFragment implements BaseAdapter.OnItemClickListener<TestEntity>{

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;

    private RecycleAdapterSearchTopic adapter;

    public static SearchTopicFragment newInstance() {
        Bundle args = new Bundle();
        SearchTopicFragment fragment = new SearchTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.home_fragment_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterSearchTopic();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                springView.onFinishFreshAndLoadDelay(1000);
            }

            @Override
            public void onLoadmore() {
                springView.onFinishFreshAndLoadDelay(1000);
            }
        });

        adapter.refreshData(new ArrayList<TestEntity>(){{
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
        }});
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void onItemClick(TestEntity testEntity, int position) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }
}
