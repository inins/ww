package com.frame.component.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.api.CommonService;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.common.NetParam;
import com.frame.component.entities.dto.GroupBeanDTO;
import com.frame.component.service.R;
import com.frame.component.service.R2;
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
import com.frame.utils.SizeUtils;
import com.frame.utils.Utils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.frame.component.utils.EntitiesUtil;
import com.frame.component.entities.GroupBean;
import com.frame.component.ui.adapter.TalkAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;

public class GroupListFragment extends BasicFragment implements IView {

    // 趣聊列表
    public final static int TYPE_GROUP_LIST = 0;
    // 聊天列表-搜索已加入的趣聊
    public final static int TYPE_SEARCH_GROUP = 1;
    // 聊天列表-搜索已加入的觅聊
    public final static int TYPE_SEARCH_MI = 2;
    // 搜索趣聊
    public final static int TYPE_SEARCH_ALL_GROUP = 3;

    @IntDef({
            TYPE_GROUP_LIST,
            TYPE_SEARCH_GROUP,
            TYPE_SEARCH_MI,
            TYPE_SEARCH_ALL_GROUP
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface GroupListType {
    }

    public static GroupListFragment newInstance(int userId) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GroupListFragment newSearchGroup() {
        GroupListFragment fragment = new GroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", TYPE_SEARCH_GROUP);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GroupListFragment newSearchMi() {
        GroupListFragment fragment = new GroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", TYPE_SEARCH_MI);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GroupListFragment newSearchAll() {
        GroupListFragment fragment = new GroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", TYPE_SEARCH_ALL_GROUP);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private TalkAdapter mAdapter;

    private int mUserId;
    private ApiHelper mApiHelper = new ApiHelper();
    private IRepositoryManager mRepositoryManager;
    private int mCurrent = 0;
    private static final int mSize = 10;
    private List<GroupBean> mList = new ArrayList<>();

    private @GroupListType
    int mType = TYPE_GROUP_LIST;
    private String mKey;
    private String mTags;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.layout_spring_recycler;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();

        if (null != getArguments()) {
            mUserId = getArguments().getInt("userid");
            mType = getArguments().getInt("type");
        }

        mAdapter = new TalkAdapter(mRecyclerView, mList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(
                new GridSpacingItemDecoration(2, SizeUtils.dp2px(14),
                        GridLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(mAdapter);

        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }

            @Override
            public void onLoadmore() {
                loadData(false);
            }
        });

        if (mType == TYPE_GROUP_LIST) {
            mSpringView.callFreshDelay();
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    private void loadData(boolean refresh) {
        if (mType == TYPE_SEARCH_GROUP) {
            chatListSearchGroup(mKey, refresh);
        } else if (mType == TYPE_SEARCH_MI) {
            chatListSearchMiList(mKey, refresh);
        } else if (mType == TYPE_SEARCH_ALL_GROUP) {
            searchGroup(mKey, mTags, refresh);
        } else {
            loadGroupList(refresh);
        }
    }

    private void refreshList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mList.clear();
        }
    }

    private void updateList(PageList<GroupBean> list) {
        if (null != list) {
            mCurrent = list.getCurrent();

            if (null != list.getList()) {
                mList.addAll(list.getList());
            }
        }

        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 用户趣聊列表
     *
     * @param refresh 是否刷新
     */
    private void loadGroupList(boolean refresh) {
        refreshList(refresh);

        mApiHelper.execute(this,
                getGroupList(mUserId, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<GroupBean>>() {
                    @Override
                    public void onNext(PageList<GroupBean> list) {
                        updateList(list);
                    }
                },
                disposable -> {
                },
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    private Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> getGroupList(int queryUserId, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("queryUserId", queryUserId)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .getGroupList(param);
    }

    /**
     * 聊天列表-搜索已加入的趣聊
     *
     * @param key     关键字
     * @param refresh 是否刷新
     */
    private void chatListSearchGroup(String key, boolean refresh) {
        refreshList(refresh);

        mApiHelper.execute(this,
                chatListSearchGroup(key, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<GroupBean>>() {
                    @Override
                    public void onNext(PageList<GroupBean> list) {
                        updateList(list);
                    }
                },
                disposable -> {
                },
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    /**
     * 聊天列表-搜索已加入的趣聊
     */
    private Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> chatListSearchGroup(String key,
                                                                                           int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("key", EntitiesUtil.assertNotNull(key))
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .chatListSearchGroup(param);
    }


    /**
     * 聊天列表-搜索已加入的觅聊
     *
     * @param key     关键字
     * @param refresh 是否刷新
     */
    private void chatListSearchMiList(String key, boolean refresh) {
        refreshList(refresh);

        mApiHelper.execute(this,
                chatListSearchMiList(key, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<GroupBean>>() {
                    @Override
                    public void onNext(PageList<GroupBean> list) {
                        updateList(list);
                    }
                },
                disposable -> {
                },
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    private Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> chatListSearchMiList(String key,
                         int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("key", EntitiesUtil.assertNotNull(key))
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .chatListSearchMiList(param);
    }

    /**
     * 搜索趣聊
     * @param keyword 关键字
     * @param tagNames 标签中文名，多个以逗号隔开
     * @param refresh 是否刷新
     */
    private void searchGroup(String keyword, String tagNames, boolean refresh) {
        refreshList(refresh);

        mApiHelper.execute(this,
                netSearchGroup(keyword, tagNames, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<GroupBean>>() {
                    @Override
                    public void onNext(PageList<GroupBean> list) {
                        updateList(list);
                    }
                },
                disposable -> {
                },
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    /**
     * 搜索趣聊
     */
    private Observable<BaseJson<PageListDTO<GroupBeanDTO, GroupBean>>> netSearchGroup(String keyword, String tagNames,
                                                                                            int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("keyword", keyword)
                .put("tagNames", tagNames)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .searchGroup(param);
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
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        super.onCommonEvent(event);

        switch (event.getEvent()) {
            case EventBean.EVENT_IM_SEARCH:
                mKey = (String) event.get("key");
                mTags = (String) event.get("tags");

                mSpringView.callFreshDelay();

                break;
            case EventBean.EVENT_APP_SEARCH:
                mKey = (String) event.get("key");
//                String tags = (String) event.get("tags");

                mSpringView.callFreshDelay();

                break;
        }
    }
}
