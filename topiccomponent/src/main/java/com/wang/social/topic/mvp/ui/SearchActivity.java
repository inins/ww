package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.frame.component.entities.Topic;
import com.frame.component.ui.acticity.PersonalCard.ui.adapter.TopicListAdapter;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.ConerEditText;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerSearchComponent;
import com.wang.social.topic.di.module.SearchModule;
import com.wang.social.topic.mvp.contract.SearchContract;
import com.wang.social.topic.mvp.model.entities.SearchResult;
import com.wang.social.topic.mvp.presenter.SearchPresenter;
import com.wang.social.topic.mvp.ui.adapter.SearchResultAdapter;
import com.wang.social.topic.mvp.ui.widget.DFShopping;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseAppActivity<SearchPresenter> implements SearchContract.View {
    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;
    // 输入框
    @BindView(R2.id.edit_search)
    ConerEditText mSearchET;

    private String mKeyword;
    private String mTags;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSearchComponent.builder()
                .appComponent(appComponent)
                .searchModule(new SearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mSearchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mTags = "";
                    for (int i = 0; i < mSearchET.getTags().size(); i++) {
                        mTags += (i == 0 ? "" : ",") + mSearchET.getTags().get(i);
                    }
                    mKeyword = mSearchET.getKey();

                    // 清空搜索框
//                    mSearchET.setText("");
                    mSpringView.callFreshDelay();
//                    mPresenter.searchTopic(mKeyword, mTags, true);
                }

                return false;
            }
        });

        mAdapter = new TopicListAdapter(this, getSupportFragmentManager(), mRecyclerView, mPresenter.getResultList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(
                new WrapContentLinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false));

        mAdapter.setClickListener(new TopicListAdapter.ClickListener() {
            @Override
            public boolean autoTopicClick() {
                return true;
            }

            @Override
            public void onTopicClick(Topic topic) {

            }

            @Override
            public void onPayTopicSuccess(Topic topic) {

            }
        });

        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.searchTopic(mKeyword, mTags, true);
            }

            @Override
            public void onLoadmore() {
                mPresenter.searchTopic(mKeyword, mTags, false);
            }
        });
    }


    // 右上角取消
    @OnClick(R2.id.cancel_text_view)
    public void quit() {
        finish();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void onSearchSuccess() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSearchCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
