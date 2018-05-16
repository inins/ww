package com.wang.social.im.mvp.ui.PersonalCard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.mvp.ui.PersonalCard.contract.FriendListContract;
import com.wang.social.im.mvp.ui.PersonalCard.di.DaggerFriendListComponent;
import com.wang.social.im.mvp.ui.PersonalCard.di.FriendListModule;
import com.wang.social.im.mvp.ui.PersonalCard.presenter.FriendListPresenter;
import com.wang.social.im.mvp.ui.PersonalCard.ui.WrapContentLinearLayoutManager;
import com.frame.component.ui.adapter.UserListAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import com.wang.social.im.R;
import com.wang.social.im.R2;

public class FriendListFragment extends BaseFragment<FriendListPresenter> implements
        FriendListContract.View {

    public final static int TYPE_FRIEND_LIST = 1;
    public final static int TYPE_SEARCH = 2;
    @IntDef({
            TYPE_FRIEND_LIST,
            TYPE_SEARCH
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FriendListType {}


    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;
    private @FriendListType int mType;

    public static FriendListFragment newInstance(int userId) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FriendListFragment newSearchInstance() {
        FriendListFragment fragment = new FriendListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("type", TYPE_SEARCH);

        return fragment;
    }

    // 用户id
    private int mUserId;
    // 搜索关键字
    private String mKey;
    private String mPhone;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFriendListComponent.builder()
                .appComponent(appComponent)
                .friendListModule(new FriendListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_personal_card_fragment_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (null != getArguments()) {
            mUserId = getArguments().getInt("userid");
            mType = getArguments().getInt("type");
            if (mType <= 0) {
                mType = TYPE_FRIEND_LIST;
            }
        }


        mAdapter = new UserListAdapter(mRecyclerView, mPresenter.getUserInfoList());
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
        if (mType == TYPE_SEARCH) {
            mPresenter.searchUser(mKey, mPhone, refresh);
        } else {
            mPresenter.loadUserFriendList(mUserId, refresh);
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

    @Override
    public void onLoadFriendListSuccess() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadFriendListCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();

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
                mKey = (String)event.get("key");
                String tags = (String)event.get("tags");

                mSpringView.callFreshDelay();

                break;
        }
    }
}
