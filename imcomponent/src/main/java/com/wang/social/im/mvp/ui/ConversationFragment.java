package com.wang.social.im.mvp.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.view.View;

import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerConversationComponent;
import com.wang.social.im.di.modules.ConversationModule;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.presenter.ConversationPresenter;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;
import com.wang.social.im.mvp.ui.adapters.holders.BaseMessageViewHolder;
import com.wang.social.im.view.IMInputView;
import com.wang.social.im.view.plugin.PluginModule;
import com.wang.social.im.widget.MessageHandlePopup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ======================================
 * 会话界面
 * <p>
 * Create by ChenJing on 2018-04-03 15:14
 * ======================================
 */
public class ConversationFragment extends BaseFragment<ConversationPresenter> implements ConversationContract.View, IMInputView.IInputViewListener, BaseMessageViewHolder.OnHandleListener {

    @BindView(R2.id.fc_message_list)
    RecyclerView fcMessageList;
    @BindView(R2.id.fc_input)
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
        ((SimpleItemAnimator) fcMessageList.getItemAnimator()).setSupportsChangeAnimations(false);
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
        mAdapter.setHandleListener(this);
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
                } else if (event.getAction() == MotionEvent.ACTION_MOVE && event.getY() - mTouchDownY > SizeUtils.dp2px(50)) {
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
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConversation = null;
        mConversationType = null;
        mAdapter = null;
        mLayoutManager = null;
        mTargetId = null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessages(List<UIMessage> uiMessages) {
        boolean needScroll = true;
        int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        if (mAdapter.getItemCount() - lastVisiblePosition > 3) {
            needScroll = false;
        }
        if (mAdapter.getData() == null) {
            mAdapter.refreshData(uiMessages);
        } else {
            mAdapter.addItem(uiMessages);
        }
        //如果最新消息没有显示在可见区域则不进行滚动
        if (needScroll) {
            fcMessageList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void showMessage(UIMessage uiMessage) {
        if (mAdapter.getData() == null) {
            List<UIMessage> list = new ArrayList<>();
            list.add(uiMessage);
            mAdapter.refreshData(list);
        } else {
            mAdapter.addItem(uiMessage);
        }

        fcMessageList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
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
    public void refreshMessage(UIMessage uiMessage) {
        int position = mAdapter.findPosition(uiMessage);
        if (position != -1) {
            mAdapter.notifyItemChanged(position);
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
        int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        if (mAdapter.getItemCount() - lastVisiblePosition < 5) {
            fcMessageList.scrollToPosition(mAdapter.getData().size() - 1);
        }
    }

    @Override
    public void onInputViewCollapsed() {
    }

    @Override
    public void onErrorClick(int position, UIMessage uiMessage) {
        //将此条消息重新发送
        mConversation.sendMessage(uiMessage.getTimMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                mAdapter.notifyDataSetChanged();
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContentClick(View view, UIMessage uiMessage, int position) {

    }

    @Override
    public void onContentLongClick(View view, UIMessage uiMessage, int position) {
        boolean showTop = false;
        if (position > mLayoutManager.findFirstVisibleItemPosition()) {
            showTop = true;
        }
        MessageHandlePopup handlePopup = new MessageHandlePopup(mActivity, uiMessage.getTimMessage().isSelf(), uiMessage, showTop);
        handlePopup.setHandleListener(new MessageHandlePopup.OnHandleListener() {
            @Override
            public void onDelete(UIMessage uiMessage) {
                mPresenter.deleteMessage(uiMessage);
            }

            @Override
            public void onWithdraw(UIMessage uiMessage) {
                mPresenter.withDrawMessage(uiMessage);
            }
        });

        int y = 0;
        if (showTop) {
            y = -(view.getHeight() + SizeUtils.dp2px(18));
        }
        int x = view.getWidth() / 2;
        if (uiMessage.getTimMessage().isSelf()) {
            x = x - getResources().getDimensionPixelOffset(R.dimen.im_msg_handle_popup_width) / 2;
        } else {
            x = x - getResources().getDimensionPixelOffset(R.dimen.im_msg_handle_popup_width_narrow) / 2;
        }
        handlePopup.showAsDropDown(view, x, y);
    }

    @Override
    public void onPortraitClick(View view, UIMessage uiMessage, int position) {

    }

    @Override
    public void onPortraitLongClick(View view, UIMessage uiMessage, int position) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TIMMessageLocator messageLocator) {
        if (mAdapter == null) {
            return;
        }
        for (UIMessage uiMessage : mAdapter.getData()) {
            TIMMessageExt messageExt = new TIMMessageExt(uiMessage.getTimMessage());
            if (messageExt.checkEquals(messageLocator)) {
                //执行删除消息
                mPresenter.deleteMessage(uiMessage);
                mPresenter.addRevokeNotifyMsg(uiMessage, false);
                break;
            }
        }
    }
}