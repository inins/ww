package com.wang.social.im.mvp.presenter;

import com.frame.component.entities.SettingInfo;
import com.frame.component.entities.dto.SettingInfoDTO;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.contract.ConversationListContract;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.UIConversation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.wang.social.im.app.IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-16 20:09
 * ============================================
 */
@FragmentScope
public class ConversationListPresenter extends BasePresenter<ConversationListContract.Model, ConversationListContract.View> implements TIMMessageListener {

    @Inject
    ApiHelper mApiHelper;

    @Inject
    public ConversationListPresenter(ConversationListContract.Model model, ConversationListContract.View view) {
        super(model, view);

        TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TIMManager.getInstance().removeMessageListener(this);
    }

    /**
     * 获取会话列表
     */
    public void getConversationList() {
        List<TIMConversation> conversations = TIMManagerExt.getInstance().getConversationList();
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : conversations) {
            if ((conversation.getType() != TIMConversationType.Group && conversation.getType() != TIMConversationType.C2C) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_MIRROR) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_GAME) ||
                    (conversation.getType() == TIMConversationType.C2C && conversation.getPeer().equals(SERVER_PUSH_MESSAGE_ACCOUNT))) {
                continue;
            }
            result.add(conversation);
            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            conversationExt.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Timber.tag(TAG).w("Conversation get message error," + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages != null && timMessages.size() > 0) {
                        mRootView.updateMessage(timMessages.get(0));
                    }
                }
            });
        }
        mRootView.initList(result);
    }

    /**
     * 新消息
     *
     * @param list
     * @return
     */
    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        mRootView.updateMessages(list);
        return false;
    }

    /**
     * 删除会话
     *
     * @param uiConversation
     */
    public void deleteConversation(UIConversation uiConversation) {
        Observable.just(uiConversation)
                .subscribeOn(Schedulers.io())
                .map(new Function<UIConversation, Boolean>() {
                    @Override
                    public Boolean apply(UIConversation uiConversation) throws Exception {
                        return TIMManagerExt.getInstance().deleteConversation(uiConversation.getMConversation().getType(), uiConversation.getIdentify());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mRootView.onDeleted(uiConversation);
                        } else {
                            ToastUtil.showToastShort("删除失败");
                        }
                    }
                });
    }

    public void getFriendsList() {
        mApiHelper.execute(mRootView, mModel.getFriendList(),
                new ErrorHandleSubscriber<ListData<IndexFriendInfo>>() {
                    @Override
                    public void onNext(ListData<IndexFriendInfo> friendInfoListData) {
                        if (friendInfoListData.getList().isEmpty()) {
                            mRootView.showNobody();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof NullPointerException) {
                            mRootView.showNobody();
                        }
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 获取用户设置信息
     * 设置离线推送
     */
    public void getUserSettingInfo() {
        mApiHelper.execute(mRootView, mModel.getUserSetting(),
                new ErrorHandleSubscriber<SettingInfo>() {
                    @Override
                    public void onNext(SettingInfo settingInfo) {
                        ImHelper.setOfflineMessagePushStatus(settingInfo.isPushEnable());
                    }
                });
    }
}