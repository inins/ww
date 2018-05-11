package com.frame.component.ui.acticity.PersonalCard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.acticity.PersonalCard.contract.FriendListContract;
import com.frame.component.ui.acticity.PersonalCard.di.DaggerFriendListComponent;
import com.frame.component.ui.acticity.PersonalCard.di.FriendListModule;
import com.frame.component.ui.acticity.PersonalCard.presenter.FriendListPresenter;
import com.frame.component.ui.acticity.PersonalCard.ui.WrapContentLinearLayoutManager;
import com.frame.component.ui.acticity.PersonalCard.ui.adapter.UserListAdapter;
import com.frame.di.component.AppComponent;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import butterknife.BindView;

public class FriendListFragment extends BaseFragment<FriendListPresenter> implements
        FriendListContract.View {



    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;

    public static FriendListFragment newInstance(int userId) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mUserId;

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
        return R.layout.personal_card_fragment_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mUserId = getArguments().getInt("userid");

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
                mPresenter.loadUserFriendList(mUserId, true);
            }

            @Override
            public void onLoadmore() {
                mPresenter.loadUserFriendList(mUserId, false);
            }
        });
        mSpringView.callFreshDelay();
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
}
