package com.frame.component.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.Topic;
import com.frame.component.entities.dto.TopicDTO;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageList;
import com.frame.http.api.PageListDTO;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.frame.component.ui.adapter.TopicListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * 话题列表
 * 话题列表 （他人名片）
 */
public class TopicListFragment extends BasicFragment implements IView {
    // 个人话题搜索(默认)
    public final static int TYPE_PERSON_TOPIC_SEARCH = 0;
    // 广场话题搜索
    public final static int TYPE_SQUARE_SEARCH = 1;
    // 话题页面话题搜索
    public final static int TYPE_TOPIC_SEARCH = 2;

    @IntDef({
            TYPE_PERSON_TOPIC_SEARCH,
            TYPE_SQUARE_SEARCH,
            TYPE_TOPIC_SEARCH
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface TopicSearchType {}

    public static TopicListFragment newInstance(int userid) {
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(getBundle(userid, TYPE_PERSON_TOPIC_SEARCH));
        return fragment;
    }

    public static TopicListFragment newSquareSearch() {
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(getBundle(-1, TYPE_SQUARE_SEARCH));
        return fragment;
    }

    public static TopicListFragment newTopicSearch() {
        TopicListFragment fragment = new TopicListFragment();
        fragment.setArguments(getBundle(-1, TYPE_TOPIC_SEARCH));
        return fragment;
    }

    private static Bundle getBundle(int userId, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userId);
        bundle.putInt("type", type);
        return bundle;
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;

    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;

    private ApiHelper mApiHelper = new ApiHelper();
    private IRepositoryManager mRepositoryManager;
    private int mCurrent = 0;
    private static final int mSize = 10;
    private List<Topic> mList = new ArrayList<>();
    private int mUserId;
    private @TopicSearchType int mType = TYPE_PERSON_TOPIC_SEARCH;
    // 记录搜索关键字
    private String mKeys;
    private String mTags;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.layout_spring_recycler_side_14;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();

        if (getArguments() != null) {
            mUserId = getArguments().getInt("userid");
            mType = getArguments().getInt("type");
        }

        mAdapter = new TopicListAdapter(this, getActivity(), getChildFragmentManager(), mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
//                getFriendTopicList(true);
                loadData(true);
            }

            @Override
            public void onLoadmore() {
//                getFriendTopicList(false);
                loadData(false);
            }
        });

        if (mType <= TYPE_PERSON_TOPIC_SEARCH) {
            mSpringView.callFreshDelay();
        } else {
            loadingviewEx.showLackView();
        }
    }

    private void loadData(boolean refresh) {
        if (mType != TYPE_PERSON_TOPIC_SEARCH) {
            if (null != mAdapter) {
                mAdapter.setSearchTags(mTags);
                mAdapter.setSearchKeyword(mKeys);
            }
            searchTopic(mKeys, mTags, refresh);
        } else {
            getFriendTopicList(refresh);
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void refreshList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mList.clear();
        }
    }

    private void updateList(PageList<Topic> pageList) {
        if (null != pageList) {
            mCurrent = pageList.getCurrent();
            if (null != pageList.getList()) {
                mList.addAll(pageList.getList());

                loadingviewEx.showOut();

                if (null != mAdapter) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        if (mList.size() <= 0) {
            if (mType != TYPE_PERSON_TOPIC_SEARCH) {
                loadingviewEx.showFailViewNoSearch();
            } else {
                loadingviewEx.showFailViewCustomize(R.drawable.common_ic_default_notopic,
                        "还没有发布话题");
            }
        }
    }

    /**
     * 话题列表 （他人名片）
     */
    private void getFriendTopicList(boolean refresh) {
        refreshList(refresh);

        mApiHelper.execute(this,
                netGetFriendTopicList(mUserId, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<Topic>>() {
                    @Override
                    public void onNext(PageList<Topic> pageList) {
                        updateList(pageList);
                    }
                },
                disposable -> {},
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    /**
     * 话题列表 （他人名片）
     */
    private Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> netGetFriendTopicList(int queryUserId, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("queryUserId", queryUserId)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .getFriendTopicList(param);
    }

    /**
     * 话题搜索
     */
    private void searchTopic(String keyword, String tagNames, boolean refresh) {
        refreshList(refresh);

        mApiHelper.execute(this,
                netSearchTopic(keyword, tagNames, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<Topic>>() {
                    @Override
                    public void onNext(PageList<Topic> pageList) {
                        updateList(pageList);
                    }
                },
                disposable -> {},
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    private Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> netSearchTopic(String keyword, String tagNames, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("keyword",keyword)
                .put("tagNames",tagNames)
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .searchTopic(param);
    }

    public void doSearch(String keyword, String tagNames) {
        mKeys = keyword;
        mTags = tagNames;

        mSpringView.callFreshDelay();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiHelper = null;

        Timber.i("onPause");
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        super.onCommonEvent(event);
        boolean changed = false;

        switch (event.getEvent()) {
            case EventBean.EVENT_APP_SEARCH:
                if (mType != TYPE_SQUARE_SEARCH) break;

                mKeys = (String) event.get("key");
                mTags = (String) event.get("tags");

                mSpringView.callFreshDelay();

                break;
            case EventBean.EVENTBUS_ADD_TOPIC_COMMENT:
                int topicId = (int) event.get("topicId");
                int topicCommentId = (int) event.get("topicCommentId");

                Timber.i("公共-话题列表 评论增加 : " + topicId + " " + topicCommentId);

                for (Topic topic : mList) {
                    if (topic.getTopicId() == topicId) {
                        topic.setCommentTotal(topic.getCommentTotal() + 1);
                        changed = true;
                    }
                }

                break;
            case EventBean.EVENTBUS_ADD_TOPIC_SHARE:
                // 转发成功，转发量加1
                int shareTopicID = (int) event.get("topicId");

                Timber.i("公共-话题列表 话题分享 : " + shareTopicID);
                for (Topic topic : mList) {
                    if (topic.getTopicId() == shareTopicID) {
                        topic.setShareTotal(topic.getShareTotal() + 1);
                        changed = true;
                    }
                }
                break;
            case EventBean.EVENTBUS_TOPIC_SUPPORT:
                // 点赞
                int supportTopicId = (int) event.get("topicId");
                boolean isSupport = (boolean) event.get("isSupport");

                Timber.i("公共-话题列表 话题点赞 : " + supportTopicId + " " + Boolean.toString(isSupport));

                for (Topic topic : mList) {
                    if (topic.getTopicId() == supportTopicId) {
                        topic.setSupport(isSupport);
                        topic.setSupportTotal(
                                Math.max(0,
                                        topic.getSupportTotal() + (isSupport ? 1 : -1)));
                        changed = true;
                    }
                }
                break;
            case EventBean.EVENTBUS_DEL_TOPIC_SUCCESS:
                Timber.i("删除话题成功，刷新");
                int delTopicId = (int) event.get("topicId");

                changed = removeById(delTopicId);
                break;
        }

        if (changed && null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 从列表移除
     * @param topicId 话题id
     * @return 是否有话题被移除
     */
    private boolean removeById(int topicId) {
        if (null == mList) return false;
        boolean deleted = false;
        Iterator<Topic> it = mList.iterator();
        while (it.hasNext()) {
            Topic t = it.next();
            if (t.getTopicId() == topicId) {
                it.remove();
                deleted = true;
            }
        }

        return deleted;
    }
}
