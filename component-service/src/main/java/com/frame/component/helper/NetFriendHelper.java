package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetFriendHelper {


    private NetFriendHelper() {
    }

    public static NetFriendHelper newInstance() {
        return new NetFriendHelper();
    }

    /**
     * 发起添加好友申请
     */
    public void netSendFriendlyApply(IView view, int userId, String reason, OnCommonSuccessCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).sendFriendlyApply("2.0.0", String.valueOf(userId), reason),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    /**
     * 同意、拒绝添加好友
     */
    public void netAgreeFriendApply(IView view, int userId, int msgId, boolean isAgree, OnCommonSuccessCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).agreeFriendApply(userId, msgId, isAgree ? 0 : 1),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    /**
     * 同意、拒绝加入趣聊、觅聊申请 （别人申请加入我的趣聊/觅聊的）
     */
    public void netAgreeFindChatApply(IView view, int groupId, int userId, int msgId, boolean isAgree, OnCommonSuccessCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).agreeFindChatApply(groupId, userId, msgId, isAgree ? 0 : 1),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    /**
     * 同意、拒绝邀请加入趣聊、觅聊（别人邀请我的）
     */
    public void netAgreeGroupApply(IView view, int groupId, int msgId, boolean isAgree, OnCommonSuccessCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).agreeGroupApply(groupId, msgId, isAgree ? 0 : 1),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    ////////////////////////////////////

    public interface OnCommonSuccessCallback {
        void success();
    }
}
