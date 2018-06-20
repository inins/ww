package com.wang.social.im.mvp.presenter;

import android.support.annotation.NonNull;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.enums.ConversationType;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.wang.social.im.R;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.enums.CustomElemType;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.entities.AnonymousInfo;
import com.wang.social.im.mvp.model.entities.CarryUserInfo;
import com.wang.social.im.mvp.model.entities.EnvelopElemData;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.EnvelopMessageCacheInfo;
import com.wang.social.im.mvp.model.entities.GameElemData;
import com.wang.social.im.mvp.model.entities.GroupGameCheckResult;
import com.wang.social.im.mvp.model.entities.LocationAddressInfo;
import com.wang.social.im.mvp.model.entities.ShadowInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;
import com.wang.social.location.mvp.model.entities.LocationInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:44
 * ======================================
 */
@FragmentScope
public class ConversationPresenter extends BasePresenter<ConversationContract.Model, ConversationContract.View> implements TIMMessageListener {

    private static final String[] sAnonymous;

    static {
        sAnonymous = new String[]{
                "关皮皮", "贺兰静霆", "黄景瑜", "宋茜", "刘亦菲", "范丞丞", "李子璇", "孟美岐", "蔡徐坤", "陈立农"
        };
    }

    private MessageListAdapter mAdapter;
    private ConversationType mConversationType;
    private TIMConversation mConversation;
    private TIMConversationExt mConversationExt;

    @Inject
    Gson gson;
    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    private ShadowInfo mShadowInfo;
    private AnonymousInfo mAnonymousInfo;
    private String mTeamTag;

    @Inject
    public ConversationPresenter(ConversationContract.Model model, ConversationContract.View view) {
        super(model, view);

        TIMManager.getInstance().addMessageListener(this);
    }

    public void setAdapter(MessageListAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setConversationType(ConversationType conversationType) {
        this.mConversationType = conversationType;
    }

    /**
     * 设置会话对象
     *
     * @param conversation
     */
    public void setConversation(@NonNull TIMConversation conversation) {
        this.mConversation = conversation;
        this.mConversationExt = new TIMConversationExt(mConversation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mConversation = null;
        mConversationExt = null;
        mAdapter = null;
        gson = null;
        TIMManager.getInstance().removeMessageListener(this);
    }

    /**
     * 接收到新消息
     *
     * @param list
     * @return
     */
    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        if (list != null && list.size() > 0) {
            if (list.get(0).getConversation().getType() == mConversation.getType() &&
                    list.get(0).getConversation().getPeer().equals(mConversation.getPeer())) {
                mRootView.showMessages(UIMessage.obtain(list));
            }
        }
        return false;
    }

    /**
     * 获取历史消息
     */
    public void getHistoryMessage() {
        TIMMessage lastMessage = null;
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            lastMessage = mAdapter.getData().get(0).getTimMessage();
        }
        mConversationExt.getLocalMessage(20, lastMessage, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int i, String s) {
                mRootView.hideMessageLoad();
            }

            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                mRootView.hideMessageLoad();
                if (timMessages == null) {
                    return;
                }
                List<TIMMessage> messages;
                //如果为镜像聊天则显示一小时内的消息
                if (mConversationType == ConversationType.MIRROR) {
                    long time = System.currentTimeMillis();
                    messages = new ArrayList<>();
                    for (TIMMessage timMessage : timMessages) {
                        if (time - timMessage.timestamp() * 1000 < 60 * 60 * 1000) {
                            messages.add(timMessage);
                        }
                    }
                } else {
                    messages = timMessages;
                }
                mRootView.insertMessages(UIMessage.obtain(messages));
            }
        });
    }

    /**
     * 删除一条消息
     *
     * @param uiMessage
     */
    public void deleteMessage(UIMessage uiMessage) {
        if (doDeleteMessage(uiMessage)) {
            int position = mAdapter.findPosition(uiMessage);
            mAdapter.removeItem(position);
        }
    }

    /**
     * 撤回一条消息
     *
     * @param uiMessage
     */
    public void withDrawMessage(UIMessage uiMessage) {
        mConversationExt.revokeMessage(uiMessage.getTimMessage(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                if (i == IMConstants.TIM_ERROR_CODE_REVOKE_TIMEOUT ||
                        i == IMConstants.TIM_ERROR_CODE_REVOKE_TIMEOUT_) {
                    ToastUtil.showToastShort(mRootView.getContext().getString(R.string.im_toast_revoke_timeout));
                }
            }

            @Override
            public void onSuccess() {
                uiMessage.refresh();
                mRootView.refreshMessage(uiMessage);
            }
        });
    }

    /**
     * 将消息状态改为已读
     */
    public void readMessages() {
        mConversationExt.setReadMessage(null, null);
    }

    /**
     * 发送一条文本消息
     *
     * @param content
     */
    public void sendTextMessage(String content) {
        TIMMessage message = new TIMMessage();
        TIMTextElem textElem = new TIMTextElem();
        textElem.setText(content);
        message.addElement(textElem);

        doSendMessage(message);
    }

    /**
     * 发送一条语音消息
     *
     * @param soundElem
     */
    public void sendSoundMessage(TIMSoundElem soundElem) {
        TIMMessage message = new TIMMessage();
        message.addElement(soundElem);

        doSendMessage(message);
    }

    /**
     * 发送一条表情消息
     */
    public void sendFaceMessage(int index) {
        TIMMessage message = new TIMMessage();
        TIMFaceElem faceElem = new TIMFaceElem();
        faceElem.setIndex(index);
        message.addElement(faceElem);

        doSendMessage(message);
    }

    /**
     * 发送图片消息
     *
     * @param images
     */
    public void sendImageMessage(String[] images, boolean isOri) {
        for (String path : images) {
            TIMMessage timMessage = new TIMMessage();
            TIMImageElem imageElem = new TIMImageElem();
            imageElem.setLevel(isOri ? 0 : 1);
            imageElem.setPath(path);
            timMessage.addElement(imageElem);

            doSendMessage(timMessage);
        }
    }

    /**
     * 发送红包消息
     *
     * @param envelopId
     * @param message
     */
    public void sendEnvelopMessage(long envelopId, String message) {
        TIMMessage timMessage = new TIMMessage();
        TIMCustomElem envelopElem = new TIMCustomElem();
        EnvelopElemData elemData = new EnvelopElemData();
        elemData.setEnvelopId(envelopId);
        elemData.setMessage(message);
        envelopElem.setData(gson.toJson(elemData).getBytes());
        timMessage.addElement(envelopElem);

        TIMMessageOfflinePushSettings pushSettings = new TIMMessageOfflinePushSettings();
        pushSettings.setDescr("[红包]");
        timMessage.setOfflinePushSettings(pushSettings);

        doSendMessage(timMessage);
    }

    /**
     * 发送位置消息
     *
     * @param locationInfo
     */
    public void sendLocationMessage(LocationInfo locationInfo) {
        TIMMessage timMessage = new TIMMessage();
        TIMLocationElem locationElem = new TIMLocationElem();
        locationElem.setLatitude(locationInfo.getLatitude());
        locationElem.setLongitude(locationInfo.getLongitude());
        LocationAddressInfo addressInfo = new LocationAddressInfo();
        addressInfo.setPlace(locationInfo.getPlace());
        addressInfo.setAddress(locationInfo.getAddress());
        locationElem.setDesc(gson.toJson(addressInfo));
        timMessage.addElement(locationElem);

        doSendMessage(timMessage);
    }

    /**
     * 发送一个游戏消息
     *
     * @param roomId
     * @param diamond
     */
    public void sendGameMessage(String roomId, int diamond) {
        TIMMessage timMessage = new TIMMessage();
        TIMCustomElem customElem = new TIMCustomElem();
        GameElemData gameElem = new GameElemData();
        gameElem.setRoomId(roomId);
        gameElem.setDiamond(diamond);
        customElem.setData(gson.toJson(gameElem).getBytes());
        timMessage.addElement(customElem);

        TIMMessageOfflinePushSettings pushSettings = new TIMMessageOfflinePushSettings();
        pushSettings.setDescr("[摇钱树]");
        timMessage.setOfflinePushSettings(pushSettings);

        doSendMessage(timMessage);
    }

    /**
     * 获取红包详情
     *
     * @param uiMessage
     */
    public void getEnvelopInfo(UIMessage uiMessage) {
        EnvelopElemData envelopElemData = (EnvelopElemData) uiMessage.getCustomMessageElemData(CustomElemType.RED_ENVELOP, EnvelopElemData.class, gson);
        if (envelopElemData != null) {
            mApiHelper.execute(mRootView, mModel.getEnvelopInfo(envelopElemData.getEnvelopId()), new ErrorHandleSubscriber<EnvelopInfo>(mErrorHandler) {
                @Override
                public void onNext(EnvelopInfo envelopInfo) {
                    mRootView.showEnvelopDialog(uiMessage, envelopInfo);
                    TIMMessageExt messageExt = new TIMMessageExt(uiMessage.getTimMessage());
                    //检查红包状态
                    if (envelopInfo.getGotDiamond() > 0) {
                        messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_ADOPTED);
                    } else {
                        switch (envelopInfo.getStatus()) {
                            case LIVING:
                                messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_INITIAL);
                                break;
                            case EMPTY:
                                messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_EMPTY);
                                break;
                            case OVERDUE:
                                messageExt.setCustomInt(EnvelopMessageCacheInfo.STATUS_OVERDUE);
                                break;
                        }
                    }
                    mRootView.refreshMessage(uiMessage);
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
    }

    /**
     * 获取趣点信息
     *
     * @param teamId
     */
    public void getFunPoint(String teamId) {
        ApiHelperEx.execute(mRootView, false, mModel.getFunPointList(teamId),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funpoint>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funpoint>> baseListWrapBaseJson) {
                        if (baseListWrapBaseJson.getData() != null && baseListWrapBaseJson.getData().getList() != null &&
                                baseListWrapBaseJson.getData().getList().size() > 0) {
                            mRootView.showFunPoint(baseListWrapBaseJson.getData().getList().get(0));
                        }
                    }
                });
    }

    /**
     * 获取觅聊信息
     *
     * @param teamId
     */
    public void getTeamInfo(String teamId) {
        mApiHelper.execute(mRootView, mModel.getTeamInfo(teamId),
                new ErrorHandleSubscriber<TeamInfo>(mErrorHandler) {
                    @Override
                    public void onNext(TeamInfo teamInfo) {
                        if (teamInfo.getTags() != null && !teamInfo.getTags().isEmpty()) {
                            mTeamTag = teamInfo.getTags().get(0).getTagName();
                        }
                    }
                });
    }

    /**
     * 获取觅聊标签
     *
     * @return
     */
    public String getTeamTag() {
        return mTeamTag == null ? "" : mTeamTag;
    }

    /**
     * 获取趣聊分身信息
     *
     * @param socialId
     * @return
     */
    public ShadowInfo getShadowInfo(String socialId) {
        if (mShadowInfo == null) {
            mApiHelper.execute(mRootView, mModel.getShadowInfo(socialId),
                    new ErrorHandleSubscriber<ShadowInfo>(mErrorHandler) {
                        @Override
                        public void onNext(ShadowInfo shadowInfo) {
                            mShadowInfo = shadowInfo;
                            mRootView.onShadowChanged(mShadowInfo);
                        }
                    });
        }
        return mShadowInfo;
    }

    /**
     * 获取匿名信息
     */
    public void getAnonymousInfo() {
        mApiHelper.execute(mRootView, mModel.getAnonymousInfo(),
                new ErrorHandleSubscriber<AnonymousInfo>(mErrorHandler) {
                    @Override
                    public void onNext(AnonymousInfo anonymousInfo) {
                        mAnonymousInfo = anonymousInfo;
                    }
                });
    }

    /**
     * 修改分身状态
     *
     * @param socialId
     */
    public void updateShadowStatus(String socialId) {
        mApiHelper.executeNone(mRootView, mModel.updateShadowStatus(socialId, mShadowInfo.getStatus() == ShadowInfo.STATUS_CLOSE),
                new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        int newStatus = mShadowInfo.getStatus() == ShadowInfo.STATUS_CLOSE ? ShadowInfo.STATUS_OPEN : ShadowInfo.STATUS_CLOSE;
                        mShadowInfo.setStatus(newStatus);
                        mRootView.onShadowChanged(mShadowInfo);
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
     * 检测该趣聊当前是否有游戏
     *
     * @param socialId
     */
    public void checkHasGame(String socialId) {
        mApiHelper.execute(mRootView, mModel.checkHasGame(socialId),
                new ErrorHandleSubscriber<GroupGameCheckResult>(mErrorHandler) {
                    @Override
                    public void onNext(GroupGameCheckResult groupGameCheckResult) {
                        if (groupGameCheckResult.isHasGame()) {
                            mRootView.gotoGameRoom(groupGameCheckResult.getRoomId());
                        } else {
                            mRootView.showCreateGameDialog();
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
     * 执行发送
     *
     * @param message
     */
    private void doSendMessage(TIMMessage message) {
        if (mConversationType == ConversationType.SOCIAL) {
            if (mShadowInfo != null && mShadowInfo.getStatus() == ShadowInfo.STATUS_OPEN) {
                addCarryInfo(message, mConversationType, mShadowInfo.getNickname(), mShadowInfo.getPortrait());

                //设置离线推送信息内容
                TIMMessageOfflinePushSettings pushSettings = message.getOfflinePushSettings();
                if (pushSettings == null) {
                    pushSettings = new TIMMessageOfflinePushSettings();
                    pushSettings.setDescr(UIMessage.obtain(message).getSummary());
                }
                TIMMessageOfflinePushSettings.AndroidSettings androidSettings = new TIMMessageOfflinePushSettings.AndroidSettings();
                String title = mShadowInfo.getNickname() + "(" + mRootView.getConversationName() + ")";
                androidSettings.setTitle(title);
                pushSettings.setAndroidSettings(androidSettings);
                message.setOfflinePushSettings(pushSettings);
            }
        } else if (mConversationType == ConversationType.MIRROR) {
            String nickname;
            if (mAnonymousInfo != null) {
                nickname = mAnonymousInfo.getNickname();
            } else {
                int getIndex = (int) (Math.random() * 10);
                mAnonymousInfo = new AnonymousInfo();
                mAnonymousInfo.setNickname(sAnonymous[getIndex]);
                nickname = mAnonymousInfo.getNickname();
            }
            addCarryInfo(message, mConversationType, nickname, null);
        }
        mConversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                mRootView.refreshMessage(UIMessage.obtain(message));
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                mRootView.refreshMessage(UIMessage.obtain(timMessage));
            }
        });
        mRootView.showMessage(UIMessage.obtain(message));

        EventBus.getDefault().post(message);
    }

    /**
     * 添加携带信息
     *
     * @param timMessage
     * @param type
     * @param nickname
     * @param portrait
     */
    private void addCarryInfo(TIMMessage timMessage, ConversationType type, String nickname, String portrait) {
        CarryUserInfo carryInfo = new CarryUserInfo();
        switch (type) {
            case SOCIAL:
                carryInfo.setType(IMConstants.CUSTOM_ELEM_SHADOW);
                break;
            case MIRROR:
                carryInfo.setType(IMConstants.CUSTOM_ELEM_ANONYMITY);
                break;
        }
        carryInfo.setFaceUrl(portrait);
        carryInfo.setNickname(nickname);
        String data = gson.toJson(carryInfo);
        TIMCustomElem customElem = new TIMCustomElem();
        customElem.setData(data.getBytes());
        timMessage.addElement(customElem);
    }

    /**
     * 执行删除消息
     *
     * @param uiMessage
     * @return
     */
    private boolean doDeleteMessage(UIMessage uiMessage) {
        TIMMessageExt messageExt = new TIMMessageExt(uiMessage.getTimMessage());
        return messageExt.remove();
    }

    /**
     * 插入一条信息到本地库
     *
     * @param timMessage
     */
    private int insertLocalMessage(TIMMessage timMessage) {
        return mConversationExt.saveMessage(timMessage, TIMManager.getInstance().getLoginUser(), false);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShadowInfoChanged(ShadowInfo shadowInfo) {
        mShadowInfo = shadowInfo;
        mRootView.onShadowChanged(mShadowInfo);
    }
}