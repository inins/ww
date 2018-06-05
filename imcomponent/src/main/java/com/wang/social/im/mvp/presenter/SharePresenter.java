package com.wang.social.im.mvp.presenter;

import com.frame.component.enums.ConversationType;
import com.frame.component.enums.ShareSource;
import com.frame.component.helper.NetShareHelper;
import com.frame.di.scope.ActivityScope;
import com.frame.entities.EventBean;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.contract.ShareContract;
import com.wang.social.im.mvp.model.entities.GameElemData;
import com.wang.social.im.mvp.model.entities.ShareElemData;
import com.wang.social.im.mvp.model.entities.UIConversation;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.wang.social.im.app.IMConstants.SERVER_PUSH_MESSAGE_ACCOUNT;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-25 11:15
 * ============================================
 */
@ActivityScope
public class SharePresenter extends BasePresenter {

    @Inject
    Gson gson;

    @Inject
    public SharePresenter() {
    }

    public void setRootView(ShareContract.View view) {
        this.mRootView = view;
        onStart();
    }

    /**
     * 获取最近联系人列表
     */
    public void getRecentlyContactList() {
        List<TIMConversation> conversations = TIMManagerExt.getInstance().getConversationList();
        List<UIConversation> result = new ArrayList<>();
        for (TIMConversation conversation : conversations) {
            if ((conversation.getType() != TIMConversationType.Group && conversation.getType() != TIMConversationType.C2C) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_MIRROR) ||
                    conversation.getPeer().startsWith(IMConstants.IM_IDENTITY_PREFIX_GAME) ||
                    (conversation.getType() == TIMConversationType.C2C && conversation.getPeer().equals(SERVER_PUSH_MESSAGE_ACCOUNT))) {
                continue;
            }
            result.add(new UIConversation(conversation));
        }
        Collections.sort(result);
        ((ShareContract.View) mRootView).showContacts(result);
    }

    /**
     * 发送消息
     *
     * @param title
     * @param content
     * @param cover
     * @param objectId
     * @param shareSource
     */
    public void sendMessage(String targetId, ConversationType conversationType, String title, String content, String cover, String objectId, ShareSource shareSource) {
        TIMMessage timMessage = new TIMMessage();
        if (shareSource == ShareSource.SOURCE_GAME_TREE) {
            try {
                timMessage.addElement(buildGameMessage(objectId, Integer.valueOf(content)));
            } catch (Exception e) {
                ToastUtil.showToastShort("分享失败");
                e.printStackTrace();
            }
        } else {
            timMessage.addElement(buildShareMessage(objectId, title, content, cover, shareSource));
        }
        TIMConversation conversation = TIMManager.getInstance().getConversation(conversationType == ConversationType.PRIVATE ? TIMConversationType.C2C : TIMConversationType.Group, targetId);
        conversation.sendMessage(timMessage, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.showToastShort("分享失败");
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                //通知会话列表更新显示
                EventBus.getDefault().post(timMessage);

                if (shareSource == ShareSource.SOURCE_FUN_SHOW || shareSource == ShareSource.SOURCE_TOPIC) {
                    Integer targetUid = null;
                    if (conversationType == ConversationType.PRIVATE) {
                        targetUid = Integer.valueOf(targetId);
                    }
                    String type;
                    if (shareSource == ShareSource.SOURCE_TOPIC) {
                        type = NetShareHelper.SHARE_TYPE_TOPIC;
                    } else {
                        type = NetShareHelper.SHARE_TYPE_FUN_SHOW;
                    }
                    NetShareHelper.newInstance()
                            .netShare(mRootView, targetUid, Integer.valueOf(objectId), type, 0, new NetShareHelper.OnShareCallback() {
                                @Override
                                public void success() {
                                    ToastUtil.showToastShort("分享成功");
                                    ((ShareContract.View) mRootView).onShareComplete();

                                    if (shareSource == ShareSource.SOURCE_FUN_SHOW) {
                                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_DETAIL_ADD_SHARE));
                                    } else {
                                        EventBus.getDefault().post(new EventBean(EventBean.EVENTBUS_ADD_TOPIC_SHARE));
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * 构建游戏消息内容
     *
     * @param roomId
     * @param diamond
     */
    private TIMElem buildGameMessage(String roomId, int diamond) {
        TIMCustomElem customElem = new TIMCustomElem();
        GameElemData gameElem = new GameElemData();
        gameElem.setRoomId(roomId);
        gameElem.setDiamond(diamond);
        customElem.setData(gson.toJson(gameElem).getBytes());
        return customElem;
    }

    /**
     * 构建分享消息内容
     *
     * @param objectId
     * @param title
     * @param content
     * @param cover
     * @param source
     * @return
     */
    private TIMElem buildShareMessage(String objectId, String title, String content, String cover, ShareSource source) {
        TIMCustomElem customElem = new TIMCustomElem();
        ShareElemData elemData = new ShareElemData();
        switch (source) {
            case SOURCE_TOPIC:
                elemData.setType(IMConstants.CUSTOM_ELEM_SHARE_TOPIC);
                break;
            case SOURCE_FUN_SHOW:
                elemData.setType(IMConstants.CUSTOM_ELEM_SHARE_FUN_SHOW);
                break;
        }
        elemData.setTitle(title);
        elemData.setContent(content);
        elemData.setObjectId(objectId);
        elemData.setCover(cover);
        customElem.setData(gson.toJson(elemData).getBytes());
        return customElem;
    }
}
