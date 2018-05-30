package com.wang.social.topic.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.component.entities.Topic;
import com.frame.component.ui.adapter.TopicListAdapter;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicListComponent;
import com.wang.social.topic.di.module.TopicListModule;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.presenter.TopicListPresenter;
import com.wang.social.topic.mvp.ui.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @IntDef({FRAGMENT_NEW, FRAGMENT_HOT, FRAGMENT_LOW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FragmentType {
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;

    TopicListAdapter mAdapter;

    // onResume 调用后才开始加载
    boolean mIsLoaded = false;
    // 类型
    @FragmentType
    int mFragmentType = FRAGMENT_NEW;

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

        mAdapter = new TopicListAdapter(getActivity() instanceof IView ? (IView) getActivity() : this,
                getActivity(),
                getChildFragmentManager(),
                mPresenter.getTopicList());
//        mAdapter.setClickListener(new TopicListAdapter.ClickListener() {
//            @Override
//            public boolean autoTopicClick() {
//                return true;
//            }
//
//            @Override
//            public void onTopicClick(Topic topic) {
//
//            }
//
//            @Override
//            public void onPayTopicSuccess(Topic topic) {
//
//            }
//        });

        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
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


    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(EventBean event) {
//        Timber.i("EventBuss 事件通知");
        boolean changed = false;

        switch (event.getEvent()) {
            case EventBean.EVENTBUS_TAG_ALL:
                Timber.i("大量知识");
                if (mFragmentType == FRAGMENT_NEW) {
                    Timber.i("刷新最新话题列表");

                    mPresenter.setTagAll(true);
                    mSpringView.callFreshDelay();
                }
                break;
            case EventBean.EVENTBUS_TAG_UPDATED:
                Timber.i("标签更新");

                mSpringView.callFreshDelay();
                break;
            case EventBean.EVENTBUS_ADD_TOPIC_COMMENT:
                int topicId = (int) event.get("topicId");
                int topicCommentId = (int) event.get("topicCommentId");

                Timber.i("话题-话题列表 评论增加 : " + topicId + " " + topicCommentId);

                for (Topic topic : mPresenter.getTopicList()) {
                    if (topic.getTopicId() == topicId) {
                        Timber.i("话题-话题列表 评论增加 更新 : " + topicId + " " + topicCommentId);
                        topic.setCommentTotal(topic.getCommentTotal() + 1);
                        changed = true;
                    }
                }
                if (changed && null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case EventBean.EVENTBUS_ADD_TOPIC_SHARE:
                // 转发成功，转发量加1
                int shareTopicID = (int) event.get("topicId");
                Timber.i("话题列表-话题转发 : " + shareTopicID);
                for (Topic topic : mPresenter.getTopicList()) {
                    if (topic.getTopicId() == shareTopicID) {
                        Timber.i("话题列表-话题转发 更新 : " + shareTopicID);
                        topic.setShareTotal(topic.getShareTotal() + 1);
                        changed = true;
                    }
                }
                break;
            case EventBean.EVENTBUS_TOPIC_SUPPORT:
                // 点赞
                int supportTopicId = (int) event.get("topicId");
                boolean isSupport = (boolean) event.get("isSupport");
                Timber.i("话题点赞 : " + supportTopicId + " " + Boolean.toString(isSupport));
                for (Topic topic : mPresenter.getTopicList()) {
                    if (topic.getTopicId() == supportTopicId) {
                        Timber.i("话题列表-话题点赞 更新 : "  + supportTopicId + " " + Boolean.toString(isSupport));
                        topic.setSupport(isSupport);
                        topic.setSupportTotal(
                                Math.max(0,
                                        topic.getSupportTotal() + (isSupport ? 1 : -1)));
                        changed = true;
                    }
                }
                break;
        }

        if (changed && null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }


}
