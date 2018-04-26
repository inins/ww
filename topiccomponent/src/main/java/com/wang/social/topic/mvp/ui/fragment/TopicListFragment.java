package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;
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
import com.wang.social.topic.mvp.ui.widget.DFShopping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import timber.log.Timber;

public class TopicListFragment extends BaseFragment<TopicListPresenter> implements TopicListContract.View {
    public final static String KEY_TYPE = "TYPE";

    public static TopicListFragment newInstance(@FragmentType int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final int FRAGMENT_NEW = 0;
    public static final int FRAGMENT_HOT = 1;
    public static final int FRAGMENT_LOW = 2;
    @IntDef({ FRAGMENT_NEW, FRAGMENT_HOT, FRAGMENT_LOW })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentType {}

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    TopicListAdapter mAdapter;

    // onResume 调用后才开始加载
    boolean mIsLoaded = false;
    // 类型
    @FragmentType int mFragmentType = FRAGMENT_NEW;

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
        if (null != getArguments()) {
            mFragmentType = getArguments().getInt(KEY_TYPE, FRAGMENT_NEW);
        }

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
                        if (topic.getIsShopping() == 1) {
                            // 需要支付
                            DFShopping.showDialog(getActivity().getSupportFragmentManager(), topic);
                        } else {
                            TopicDetailActivity.start(getActivity(), topic.getTopicId());
                        }
                    }
                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        // 刷新和加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadTopic(true);
            }

            @Override
            public void onLoadmore() {
                loadTopic(false);
            }
        });
    }

    private void loadTopic(boolean refresh) {
        if (refresh) {
            mPresenter.clearTopicList();
        }

        switch (mFragmentType) {
            case FRAGMENT_NEW:
                mPresenter.getNewsList();
                break;
            case FRAGMENT_HOT:
                mPresenter.getHotList();
                break;
            case FRAGMENT_LOW:
                mPresenter.getLowList();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Timber.i("onResume");

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
