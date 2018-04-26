package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicListComponent;
import com.wang.social.topic.di.module.TopicListModule;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.model.entities.Topic;
import com.wang.social.topic.mvp.presenter.TopicListPresenter;
import com.wang.social.topic.mvp.ui.TopicDetailActivity;
import com.wang.social.topic.mvp.ui.adapter.TopicListAdapter;

import butterknife.BindView;

public class TopicListFragment extends BaseFragment<TopicListPresenter> implements TopicListContract.View {

    public static TopicListFragment newInstance() {
        return new TopicListFragment();
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    TopicListAdapter mAdapter;

    boolean mIsLoaded = false;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTopicListComponent.builder()
                .appComponent(appComponent)
                .topicListModule(new TopicListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.topic_fragment_topic_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mAdapter = new TopicListAdapter(getContext(), new TopicListAdapter.DataProvider() {
            @Override
            public Topic getTopic(int position) {
                return mPresenter.getTopic(position);
            }

            @Override
            public int getTopicCount() {
                return mPresenter.getTopicCount();
            }
        },
                new TopicListAdapter.ClickListener() {
                    @Override
                    public void onTopicClick(Topic topic) {
                        TopicDetailActivity.start(getActivity(), topic.getTopicId());
                    }
                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.clearTopicList();
                mPresenter.getNewsList();
            }

            @Override
            public void onLoadmore() {
                mPresenter.getNewsList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mIsLoaded) {
            mSpringView.callFreshDelay();
            mIsLoaded = true;
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mAdapter) {
            mAdapter.onDestroy();
            mAdapter = null;
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onTopicLoadSuccess() {
        refreshTopicList();
    }

    @Override
    public void onTopicLoadCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();
    }

    @Override
    public void refreshTopicList() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
