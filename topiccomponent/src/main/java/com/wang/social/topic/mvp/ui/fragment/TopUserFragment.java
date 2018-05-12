package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicComponent;
import com.wang.social.topic.di.module.TopicModule;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.presenter.TopicPresenter;
import com.wang.social.topic.mvp.ui.WrapContentLinearLayoutManager;
import com.wang.social.topic.mvp.ui.adapter.TopUserAdapter;

import java.util.List;

import butterknife.BindView;

public class TopUserFragment extends BaseFragment<TopicPresenter> implements TopicContract.View {


    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    TopUserAdapter mAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTopicComponent.builder()
                .appComponent(appComponent)
                .topicModule(new TopicModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_fragment_top_user;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    getActivity().finish();
                }
            }
        });

        mAdapter = new TopUserAdapter(mRecyclerView, mPresenter.getTopicTopUserList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(
                new WrapContentLinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL, false));


        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getReleaseTopicTopUser(true);
            }

            @Override
            public void onLoadmore() {
                mPresenter.getReleaseTopicTopUser(false);
            }
        });
        mSpringView.callFreshDelay();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void onMyRecommendTagListLoad(List<Tag> list) {

    }

    @Override
    public void onTopicTopUserLoadSuccess() {
        refreshTopUserList();
    }

    @Override
    public void onTopicTopUserLoadCompleted() {

        mSpringView.onFinishFreshAndLoadDelay();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void refreshTopUserList() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
