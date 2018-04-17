package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.tencent.imsdk.TIMConversation;
import com.wang.social.im.R;
import com.wang.social.im.di.component.DaggerConversationListComponent;
import com.wang.social.im.di.modules.ConversationListModule;
import com.wang.social.im.mvp.contract.ConversationListContract;
import com.wang.social.im.mvp.presenter.ConversationListPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * ============================================
 * 会话列表
 * <p>
 * Create by ChenJing on 2018-04-16 20:04
 * ============================================
 */
public class ConversationListFragment extends BaseFragment<ConversationListPresenter> implements ConversationListContract.View {

    @BindView(R.id.cvl_conversation_list)
    RecyclerView cvlConversationList;

    private LinearLayoutManager mLayoutManager;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerConversationListComponent.builder()
                .appComponent(appComponent)
                .conversationListModule(new ConversationListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_conversation_list;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initialView();
    }

    private void initialView() {
        mLayoutManager = new LinearLayoutManager(mActivity);
        cvlConversationList.setLayoutManager(mLayoutManager);
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
    public void initList(List<TIMConversation> conversations) {

    }
}
