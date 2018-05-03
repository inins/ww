package com.wang.social.im.mvp.presenter;

import com.frame.component.utils.UIUtil;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.wang.social.im.R;
import com.wang.social.im.mvp.contract.GroupContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.MemberInfo;

import javax.inject.Inject;

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
                        mRootView.showMembers(memberInfoListData.getList());
                    }
                });
    }

    /**
     * 清除聊天内容
     * @param groupId
     */
    public void clearMessages(String groupId){
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
            }
        });
    }
}