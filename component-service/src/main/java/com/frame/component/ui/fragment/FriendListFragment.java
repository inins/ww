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
import com.frame.component.entities.PersonalInfo;
import com.frame.component.entities.dto.PersonalInfoDTO;
import com.frame.component.entities.dto.SearchUserInfoDTO;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.utils.EntitiesUtil;
import com.frame.component.view.WrapContentLinearLayoutManager;
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
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.frame.component.ui.adapter.UserListAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;

public class FriendListFragment extends BasicFragment implements
        IView {

    public final static int TYPE_FRIEND_LIST = 1;
    public final static int TYPE_GROUP_SEARCH = 2;

    @IntDef({
            TYPE_FRIEND_LIST,
            TYPE_GROUP_SEARCH
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FriendListType {
    }

    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private @FriendListType
    int mType;

    public static FriendListFragment newInstance(int userId) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FriendListFragment newGroupSearch() {
        FriendListFragment fragment = new FriendListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("type", TYPE_GROUP_SEARCH);

        return fragment;
    }

    private ApiHelper mApiHelper;
    private IRepositoryManager mRepositoryManager;
    // 用户id
    private int mUserId;
    // 搜索关键字
    private String mKey;
    private String mPhone;
    // 每页加载量和当前页码
    private final static int mSize = 10;
    private int mCurrent = 0;
    // 用户数据列表
    private List<PersonalInfo> mUserInfoList = new ArrayList<>();

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.layout_spring_recycler;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mApiHelper = FrameUtils.obtainAppComponentFromContext(getContext()).apiHelper();
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(getContext()).repoitoryManager();

        if (null != getArguments()) {
            mUserId = getArguments().getInt("userid");
            mType = getArguments().getInt("type");
            if (mType <= 0) {
                mType = TYPE_FRIEND_LIST;
            }
        }

        mAdapter = new UserListAdapter(mRecyclerView, mUserInfoList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
//                mPresenter.loadUserFriendList(mUserId, true);
                loadData(true);
            }

            @Override
            public void onLoadmore() {
//                mPresenter.loadUserFriendList(mUserId, false);
                loadData(false);
            }
        });

        // 如果是好友列表，直接请求数据
        if (mType == TYPE_FRIEND_LIST) {
            mSpringView.callFreshDelay();
        }
    }

    private void loadData(boolean refresh) {
        if (mType == TYPE_GROUP_SEARCH) {
            searchGroupUser(mKey, mPhone, refresh);
        } else {
            loadUserFriendList(mUserId, refresh);
        }
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

    private void refreshList() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取用户信息
     *
     * @param userId  用户id
     * @param refresh 是否刷新
     */
    public void loadUserFriendList(int userId, boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mUserInfoList.clear();
        }
        mApiHelper.execute(this,
                netGetUserFriendList(userId, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<PersonalInfo>>() {
                    @Override
                    public void onNext(PageList<PersonalInfo> pageList) {
                        if (null != pageList) {
                            mCurrent = pageList.getCurrent();

                            if (null != pageList.getList()) {
                                mUserInfoList.addAll(pageList.getList());
                            }
                        }

                        refreshList();
                    }
                },
                disposable -> {
                },
                () -> mSpringView.onFinishFreshAndLoadDelay());
    }

    public Observable<BaseJson<PageListDTO<PersonalInfoDTO, PersonalInfo>>> netGetUserFriendList(int otherUserId, int current, int size) {
        Map<String, Object> param = new NetParam()
                .put("otherUserId", otherUserId)
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .getUserFriendList(param);
    }

    /**
     * 聊天列表-搜索已添加的好友
     *
     * @param key     关键字
     * @param phone   手机号
     * @param refresh 是否刷新
     */
    public void searchGroupUser(String key, String phone, boolean refresh) {
        if (refresh) {
            mCurrent = 0;
            mUserInfoList.clear();
        }

        mApiHelper.execute(this,
                netSearchGroupUser(key, phone, mCurrent + 1, mSize),
                new ErrorHandleSubscriber<PageList<PersonalInfo>>() {
                    @Override
                    public void onNext(PageList<PersonalInfo> list) {
                        if (null != list) {
                            mCurrent = list.getCurrent();

                            if (null != list.getList()) {
                                mUserInfoList.addAll(list.getList());
                            }
                        }

                        refreshList();
                    }
                },
                disposable -> {
                },
                () -> {
                });
    }

    public Observable<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>> netSearchGroupUser(
            String key, String phone,
            int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("key", EntitiesUtil.assertNotNull(key))
                .put("phone", EntitiesUtil.assertNotNull(phone))
                .put("current", current)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .chatListSearchUser(param);
    }


    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        super.onCommonEvent(event);

        switch (event.getEvent()) {
            case EventBean.EVENT_APP_SEARCH:
                mKey = (String) event.get("key");
//                String tags = (String)event.get("tags");

                mSpringView.callFreshDelay();

                break;
        }
    }
}