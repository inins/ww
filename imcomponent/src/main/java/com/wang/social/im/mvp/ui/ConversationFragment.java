package com.wang.social.im.mvp.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.wang.social.im.R;
import com.wang.social.im.di.component.DaggerConversationComponent;
import com.wang.social.im.di.modules.ConversationModule;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.presenter.ConversationPresenter;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;
import com.wang.social.im.view.IMInputView;
import com.wang.social.im.view.plugin.PluginModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

/**
 * ======================================
 * 会话界面
 * <p>
 * Create by ChenJing on 2018-04-03 15:14
 * ======================================
 */
public class ConversationFragment extends BaseFragment<ConversationPresenter> implements ConversationContract.View, IMInputView.IInputViewListener {

    @BindView(R.id.fc_message_list)
    RecyclerView fcMessageList;
    @BindView(R.id.fc_input)
    IMInputView fcInput;

    private ConversationType mConversationType;
    private String mTargetId;

    private MessageListAdapter mAdapter;
    private TIMConversation mConversation;
    private LinearLayoutManager mLayoutManager;

    private float mTouchDownY;

    public static ConversationFragment newInstance(ConversationType conversationType, String targetId) {
        Bundle args = new Bundle();
        args.putInt("type", conversationType.ordinal());
        args.putString("targetId", targetId);
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerConversationComponent.builder()
                .appComponent(appComponent)
                .conversationModule(new ConversationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_conversation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mConversationType = ConversationType.values()[getArguments().getInt("type")];
        mTargetId = getArguments().getString("targetId");

        TIMConversationType timConversationType;
        if (mConversationType == ConversationType.PRIVATE) {
            timConversationType = TIMConversationType.C2C;
        } else if (mConversationType == ConversationType.SOCIAL || mConversationType == ConversationType.TEAM || mConversationType == ConversationType.MIRROR) {
            timConversationType = TIMConversationType.Group;
        } else {
            throw new IllegalArgumentException("Unknown conversation type!");
        }
        mConversation = TIMManager.getInstance().getConversation(timConversationType, mTargetId);
        mPresenter.setConversation(mConversation);

        fcInput.setMInputViewListener(this);

        fcInput.setConversationType(mConversationType);
        mLayoutManager = new LinearLayoutManager(mActivity);
        fcMessageList.setLayoutManager(mLayoutManager);
        fcMessageList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int itemSpace = getResources().getDimensionPixelSize(R.dimen.im_msg_text_size);
                outRect.top = itemSpace;
                outRect.bottom = itemSpace;
            }
        });
        mAdapter = new MessageListAdapter(mConversationType);
        fcMessageList.setAdapter(mAdapter);

        mPresenter.setAdapter(mAdapter);

        mPresenter.getHistoryMessage();

        setListener();
    }

    private void setListener() {
        fcMessageList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mTouchDownY = event.getY();
                } else if (event.getY() - mTouchDownY > SizeUtils.dp2px(20)) {
                    fcInput.collapse();
                }
                return false;
            }
        });
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
    public void showMessages(List<UIMessage> uiMessages) {
        if (mAdapter.getData() == null) {
            mAdapter.refreshData(uiMessages);
        } else {
            mAdapter.addItem(uiMessages);
        }
        fcMessageList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void showMessage(UIMessage uiMessage) {
        boolean needScroll = true;
        int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        if (mAdapter.getItemCount() - lastVisiblePosition > 3) {
            needScroll = false;
        }
        if (mAdapter.getData() == null) {
            List<UIMessage> list = new ArrayList<>();
            list.add(uiMessage);
            mAdapter.refreshData(list);
        } else {
            mAdapter.addItem(uiMessage);
        }
        //如果最新消息没有显示在可见区域则不进行滚动
        if (needScroll) {
            fcMessageList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void insertMessages(List<UIMessage> uiMessages) {
        if (mAdapter.getData() == null) {
            mAdapter.refreshData(uiMessages);

            fcMessageList.scrollToPosition(mAdapter.getData().size() - 1);
        } else {
            mAdapter.insertItem(0, uiMessages);
        }
    }

    @Override
    public void onPluginClick(PluginModule pluginModule) {

    }

    @Override
    public void onVoiceInputTouch(View view, MotionEvent event) {

    }

    @Override
    public void onEmotionClick(String codeName, String showName) {

    }

    @Override
    public void onSendClick(String content) {
        mPresenter.sendTextMessage(content);
    }

    @Override
    public void onInputViewExpanded() {
        fcMessageList.scrollToPosition(mAdapter.getData().size() - 1);
    }

    @Override
    public void onInputViewCollapsed() {

    }
}