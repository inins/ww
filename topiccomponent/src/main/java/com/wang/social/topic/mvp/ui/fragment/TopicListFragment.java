package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.presenter.TopicListPresenter;
import com.wang.social.topic.mvp.ui.adapter.TopicListAdapter;

import butterknife.BindView;

public class TopicListFragment extends BaseFragment<TopicListPresenter> implements TopicListContract.View {

    public static TopicListFragment newInstance() {
        return new TopicListFragment();
    }

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    TopicListAdapter mAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.topic_fragment_topic_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mAdapter = new TopicListAdapter(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
