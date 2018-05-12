package com.wang.social.moneytree.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.component.entities.User;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.R2;
import com.wang.social.moneytree.mvp.model.MemberListHelper;
import com.wang.social.moneytree.mvp.model.entities.Member;
import com.wang.social.moneytree.mvp.model.entities.MemberList;
import com.wang.social.moneytree.mvp.ui.adapter.MemberListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MemberListActivity extends BaseAppActivity implements IView {
    public final static String NAME_GAME_ID = "NAME_GAME_ID";

    public static void start(Context context, int gameId) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra(NAME_GAME_ID, gameId);
        context.startActivity(intent);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    MemberListAdapter mAdapter;

    private int mGameId;
    private List<Member> mList = new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.mt_activit_member_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mGameId = getIntent().getIntExtra(NAME_GAME_ID, mGameId);

        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            }
        });

        mAdapter = new MemberListAdapter(mRecyclerView, mList);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);


        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadMemberList();
            }

            @Override
            public void onLoadmore() {
            }
        });

        mSpringView.callFreshDelay();
    }

    private void loadMemberList() {
        MemberListHelper.newInstance().getMemberList(this,
                mGameId,
                new ErrorHandleSubscriber<MemberList>() {
                    @Override
                    public void onNext(MemberList memberList) {
                        if (null != memberList && null != memberList.getList()) {
                            mList.clear();
                            mList.addAll(memberList.getList());

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                },
                new Action() {
                    @Override
                    public void run() throws Exception {
                        mSpringView.onFinishFreshAndLoadDelay();
                    }
                });
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
