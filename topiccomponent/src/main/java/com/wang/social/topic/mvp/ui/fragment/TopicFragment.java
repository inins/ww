package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicComponent;
import com.wang.social.topic.di.module.TopicModule;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.presenter.TopicPresenter;
import com.wang.social.topic.mvp.ui.adapter.SelectedTagAdapter;

import butterknife.BindView;


public class TopicFragment extends BaseFragment<TopicPresenter> implements TopicContract.View {

    // 选中标签列表
    @BindView(R2.id.selected_recycler_view)
    RecyclerView mRecyclerView;
    SelectedTagAdapter mSelectedTagAdapter;

    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager;


    public static TopicFragment newInstance() {
        Bundle args = new Bundle();
        TopicFragment fragment = new TopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.topic_fragment_topic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initSelectedTagData();
        initTopicList();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTopicComponent.builder()
                .appComponent(appComponent)
                .topicModule(new TopicModule(this))
                .build()
                .inject(this);
    }

    /**
     * 初始化已选标签信息列表
     */
    private void initSelectedTagData() {
        mSelectedTagAdapter = new SelectedTagAdapter(getContext(), new SelectedTagAdapter.DataProvider() {
            @Override
            public int getItemCount() {
                return mPresenter.getSelectedTagCount();
            }

            @Override
            public String getName(int position) {
                return mPresenter.getSelectedTagName(position);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mSelectedTagAdapter);

        // 加载数据
        mPresenter.myRecommendTag();
    }

    /**
     * 初始化话题列表
     */
    private void initTopicList() {
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TopicListFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void refreshSelectedTagLise() {
        if (null != mSelectedTagAdapter) {
            mSelectedTagAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }
}
