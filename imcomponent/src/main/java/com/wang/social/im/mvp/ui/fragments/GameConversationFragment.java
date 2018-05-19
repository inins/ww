package com.wang.social.im.mvp.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.frame.base.BaseFragment;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.di.component.DaggerGameConversationComponent;
import com.wang.social.im.di.modules.GameConversationModule;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.mvp.contract.GameConversationContract;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.presenter.GameConversationPresenter;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;
import com.wang.social.im.view.GameInputView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 10:52
 * ============================================
 */
public class GameConversationFragment extends BaseFragment<GameConversationPresenter> implements GameConversationContract.View, GameInputView.IGInputViewListener {

    @BindView(R2.id.fgc_messages)
    RecyclerView fgcMessages;
    @BindView(R2.id.fgc_loader)
    SpringView fgcLoader;
    @BindView(R2.id.fgc_input)
    GameInputView fgcInput;

    private String mIdentity;

    private TIMConversation mConversation;

    private LinearLayoutManager mLayoutManager;
    private MessageListAdapter mAdapter;

    public static GameConversationFragment newInstance(String gameId) {
        Bundle args = new Bundle();
        args.putString("gameId", gameId);
        GameConversationFragment fragment = new GameConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIdentity = IMConstants.IM_IDENTITY_PREFIX_GAME + getArguments().getString("gameId");
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerGameConversationComponent
                .builder()
                .appComponent(appComponent)
                .gameConversationModule(new GameConversationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_game_conversation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initView();

        mPresenter.joinGroup(mIdentity);

        mLayoutManager = new LinearLayoutManager(mActivity);
        fgcMessages.setLayoutManager(mLayoutManager);
        int itemSpace = UIUtil.getDimen(R.dimen.im_msg_game_item_space);
        fgcMessages.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = itemSpace;
            }
        });
        ((SimpleItemAnimator) fgcMessages.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter = new MessageListAdapter(ConversationType.GAME);
        fgcMessages.setAdapter(mAdapter);
        mPresenter.setAdapter(mAdapter);
    }

    private void initView() {
        fgcInput.setMInputViewListener(this);

        fgcLoader.setEnableFooter(false);
        fgcLoader.setEnableHeader(false);
        fgcLoader.setHeader(new AliHeader(getContext(), false));

        fgcLoader.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHistoryMessage();
            }

            @Override
            public void onLoadmore() {

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
        fgcLoader.onFinishFreshAndLoadDelay();
    }

    @Override
    public void onEmotionClick(String codeName, String showName) {

    }

    @Override
    public void onSendClick(String content) {
        mPresenter.sendTextMessage(content);
    }

    @Override
    public void onInputViewExpanded(int height) {
        int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        if (mAdapter.getItemCount() - lastVisiblePosition < 5) {
            fgcMessages.scrollToPosition(mAdapter.getData().size() - 1);
        }
        EventBean eventBean = new EventBean(EventBean.EVENT_GAME_INPUT_HEIGHT_CHANGED);
        eventBean.put("height", height);
        EventBus.getDefault().post(eventBean);
    }

    @Override
    public void onInputViewCollapsed() {

    }

    @Override
    public void joinComplete() {
        fgcLoader.setEnableHeader(true);

        mConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mIdentity);
        mPresenter.setConversation(mConversation);
    }

    @Override
    public void showMessages(List<UIMessage> uiMessages) {
        boolean needScroll = true;
        int lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        if (mAdapter.getData() != null && mAdapter.getItemCount() - lastVisiblePosition > 3) {
            needScroll = false;
        }
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            mAdapter.refreshData(uiMessages);
        } else {
            mAdapter.addItem(uiMessages);
        }
        //如果最新消息没有显示在可见区域则不进行滚动
        if (needScroll) {
            fgcMessages.smoothScrollToPosition(mAdapter.getItemCount() - 1);
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

        fgcMessages.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void insertMessages(List<UIMessage> uiMessages) {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            mAdapter.refreshData(uiMessages);

            fgcMessages.scrollToPosition(mAdapter.getData().size() - 1);
        } else {
            mAdapter.getData().addAll(0, uiMessages);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshMessage(UIMessage uiMessage) {
        int position = mAdapter.findPosition(uiMessage);
        if (position != -1) {
            int startPosition = mLayoutManager.findFirstVisibleItemPosition();
            int endPosition = mLayoutManager.findLastVisibleItemPosition();
            if (position >= startPosition && position <= endPosition) {
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (fgcInput.isExpanded()) {
            fgcInput.collapse();
            return true;
        } else {
            return super.onBackPressed();
        }
    }
}
