package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseFragment;
import com.frame.component.common.HVItemDecoration;
import com.frame.component.helper.CommonHelper;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerConversationListComponent;
import com.wang.social.im.di.modules.ConversationListModule;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.helper.StickHelper;
import com.wang.social.im.mvp.contract.ConversationListContract;
import com.wang.social.im.mvp.model.entities.UIConversation;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.presenter.ConversationListPresenter;
import com.wang.social.im.mvp.ui.adapters.ConversationAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * ============================================
 * 会话列表
 * <p>
 * Create by ChenJing on 2018-04-16 20:04
 * ============================================
 */
public class ConversationListFragment extends BaseFragment<ConversationListPresenter> implements ConversationListContract.View, BaseAdapter.OnItemClickListener<UIConversation>, ConversationAdapter.OnHandleListener {

    @BindView(R2.id.cvl_conversation_list)
    RecyclerView cvlConversationList;

    private LinearLayoutManager mLayoutManager;
    private ConversationAdapter mAdapter;
    private List<UIConversation> mConversations;

    public static ConversationListFragment newInstance() {
        Bundle args = new Bundle();
        ConversationListFragment fragment = new ConversationListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConversations = new LinkedList<>();
    }

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

        mPresenter.getConversationList();
    }

    private void initialView() {
        mLayoutManager = new LinearLayoutManager(mActivity);
        cvlConversationList.setLayoutManager(mLayoutManager);
        HVItemDecoration itemDecoration = new HVItemDecoration(getContext(), HVItemDecoration.LINEAR_DIVIDER_VERTICAL);
        itemDecoration.setLeftMargin(UIUtil.getDimen(R.dimen.common_border_margin));
        cvlConversationList.addItemDecoration(itemDecoration);
        ((SimpleItemAnimator) cvlConversationList.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new ConversationAdapter(mConversations, this);
        mAdapter.setOnItemClickListener(this);
        cvlConversationList.setAdapter(mAdapter);
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLayoutManager = null;
        mAdapter = null;
        if (mConversations != null) {
            mConversations.clear();
        }
        mConversations = null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void initList(List<TIMConversation> conversations) {
        mConversations.clear();
        for (TIMConversation item : conversations) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    mConversations.add(new UIConversation(item));
                    break;
            }
        }
    }

    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        doUpdate(message);
        refresh();
    }

    @Override
    public void updateMessages(List<TIMMessage> messages) {
        if (messages == null || messages.size() == 0) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        for (TIMMessage message : messages) {
            doUpdate(message);
        }
        refresh();
    }

    @Override
    public void onDeleted(UIConversation uiConversation) {
        //从置顶列表中移除
        StickHelper.getInstance().removeIfExit(uiConversation);
        //从会话列表中移除
        Iterator<UIConversation> iterator = mConversations.iterator();
        while (iterator.hasNext()) {
            UIConversation item = iterator.next();
            if (item.equals(uiConversation)) {
                iterator.remove();
                break;
            }
        }
        refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessage(TIMMessage message) {
        updateMessage(message);
    }

    private void refresh() {
        Collections.sort(mConversations);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(UIConversation conversation, int position) {
        switch (conversation.getConversationType()) {
            case PRIVATE:
                CommonHelper.ImHelper.gotoPrivateConversation(getContext(), conversation.getIdentify());
                break;
            case SOCIAL:
            case TEAM:
                CommonHelper.ImHelper.gotoGroupConversation(getContext(), conversation.getIdentify(), conversation.getConversationType());
                break;
            case MIRROR:
                MirrorConversationActivity.start(getContext(), conversation.getIdentify());
                break;
        }
    }

    private void doUpdate(TIMMessage message) {
        if (message.getConversation().getType() != TIMConversationType.C2C && message.getConversation().getType() != TIMConversationType.Group) {
            return;
        }
        UIConversation uiConversation = new UIConversation(message.getConversation());
        if (uiConversation.getConversationType() == ConversationType.MIRROR || uiConversation.getConversationType() == ConversationType.GAME) {
            return;
        }
        Iterator<UIConversation> iterator = mConversations.iterator();
        while (iterator.hasNext()) {
            UIConversation item = iterator.next();
            if (item.equals(uiConversation)) {
                iterator.remove();
                break;
            }
        }
        uiConversation.setLastMessage(UIMessage.obtain(message));
        mConversations.add(uiConversation);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageRevoke(TIMMessageLocator messageLocator) {
        if (mAdapter == null) {
            return;
        }
        for (UIConversation conversation : mAdapter.getData()) {
            UIMessage lastMessage = conversation.getLastMessage();
            if (lastMessage != null) {
                TIMMessageExt messageExt = new TIMMessageExt(lastMessage.getTimMessage());
                if (messageExt.checkEquals(messageLocator)) {
                    lastMessage.refresh();
                    refresh();
                    break;
                }
            }
        }
    }

    @Override
    public void toggleStick(UIConversation uiConversation, int position) {
        StickHelper.getInstance().toggleStick(uiConversation);
        refresh();
    }

    @Override
    public void onDelete(UIConversation uiConversation, int position) {
        mPresenter.deleteConversation(uiConversation);
    }
}