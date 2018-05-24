package com.wang.social.im.mvp.presenter;

import android.text.TextUtils;

import com.frame.component.helper.QiNiuManager;
import com.frame.component.utils.UIUtil;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiException;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.wang.social.im.R;
import com.wang.social.im.enums.MessageNotifyType;
import com.wang.social.im.mvp.contract.GroupContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.MemberInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.frame.entities.EventBean.EVENT_NOTIFY_CLEAR_MESSAGE;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 10:04
 * ============================================
 */
public class GroupPresenter<M extends GroupContract.GroupModel, V extends GroupContract.GroupView> extends BasePresenter<M, V> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    IRepositoryManager mRepositoryManager;

    public GroupPresenter(M model, V view) {
        super(model, view);
    }

    /**
     * 获取成员列表
     *
     * @param groupId
     */
    public void getMemberList(String groupId) {
        mApiHelper.execute(mRootView, mModel.getMemberList(groupId),
                new ErrorHandleSubscriber<ListData<MemberInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(ListData<MemberInfo> memberInfoListData) {
                        //将群主移到第一位
                        for (MemberInfo memberInfo : memberInfoListData.getList()) {
                            if (memberInfo.getRole() == MemberInfo.ROLE_MASTER) {
                                memberInfoListData.getList().remove(memberInfo);
                                memberInfoListData.getList().add(0, memberInfo);
                                break;
                            }
                        }
                        mRootView.showMembers(memberInfoListData.getList());
                    }
                });
    }

    /**
     * 清除聊天内容
     *
     * @param groupId
     */
    public void clearMessages(String groupId) {
        TIMConversation timConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
        TIMConversationExt conversationExt = new TIMConversationExt(timConversation);
        conversationExt.deleteLocalMessage(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_clear_fail));
            }

            @Override
            public void onSuccess() {
                ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_clear_success));
                EventBean eventBean = new EventBean(EVENT_NOTIFY_CLEAR_MESSAGE);
                eventBean.put("groupId", groupId);
                EventBus.getDefault().post(eventBean);
            }
        });
    }

    /**
     * 修改趣聊/觅聊名片
     *
     * @param groupId
     * @param nickname
     * @param portrait
     */
    public void updateNameCard(String groupId, String nickname, String portrait, MessageNotifyType notifyType) {
        if (!TextUtils.isEmpty(portrait) && !portrait.startsWith("http")) {
            QiNiuManager manager = new QiNiuManager(mRepositoryManager);
            mRootView.showLoading();
            manager.uploadFile(mRootView, portrait, new QiNiuManager.OnSingleUploadListener() {
                @Override
                public void onSuccess(String url) {
                    updateNameCard(groupId, nickname, url, notifyType);
                }

                @Override
                public void onFail() {
                    mRootView.hideLoading();
                    ToastUtil.showToastShort("头像上传失败");
                }
            });
        } else {
            mApiHelper.executeNone(mRootView, mModel.updateNameCard(groupId, nickname, portrait, notifyType),
                    new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                        @Override
                        public void onNext(BaseJson baseJson) {

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
     * 解散趣聊/觅聊
     *
     * @param groupId
     */
    public void dissolveGroup(String groupId) {
        mApiHelper.executeNone(mRootView, mModel.dissolveGroup(groupId),
                new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        mRootView.onExitOrDissolve();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            ToastUtil.showToastShort(((ApiException) e).getMsg());
                        } else {
                            super.onError(e);
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
     * 退出趣聊/觅聊
     *
     * @param groupId
     */
    public void exitGroup(String groupId) {
        mApiHelper.executeNone(mRootView, mModel.exitGroup(groupId),
                new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        mRootView.onExitOrDissolve();
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