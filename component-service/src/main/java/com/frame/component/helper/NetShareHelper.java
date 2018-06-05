package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetShareHelper {

    /**
     * 分享趣聊
     */
    public static final String SHARE_TYPE_GROUP = "group";
    /**
     * 分享趣晒
     */
    public static final String SHARE_TYPE_FUN_SHOW = "talk";
    /**
     * 分享话题
     */
    public static final String SHARE_TYPE_TOPIC = "topic";

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    private NetShareHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
    }

    public static NetShareHelper newInstance() {
        return new NetShareHelper();
    }

    /**
     * 分享趣晒
     */
    public void netShareFunshow(IView view, Integer targetUserId, int talkId, OnShareCallback callback) {
        netShareFunshow(view, targetUserId, talkId, 1, callback);
    }

    public void netShareFunshow(IView view, Integer targetUserId, int talkId, int shareType, OnShareCallback callback) {
        netShare(view, targetUserId, talkId, SHARE_TYPE_FUN_SHOW, shareType, callback);
    }

    /**
     * 分享话题
     *
     * @param view         IView
     * @param targetUserId 目标用户id
     * @param topicId      话题id
     * @param callback     回调
     */
    public void netShareTopic(IView view, Integer targetUserId, int topicId, int shareType, OnShareCallback callback) {
        netShare(view, targetUserId, topicId, SHARE_TYPE_TOPIC, shareType, callback);
    }

    /**
     * 分享趣晒/话题
     *
     * @param view         IView
     * @param targetUserId 分享的目标用户id(若分享至往往用户必传)
     * @param objectId     分享对象id
     * @param type         分享类型（topic:话题；group：趣聊；talk:趣晒; ）
     * @param shareType    0:app内部分享，1：外部分享
     * @param callback     回调
     */
    public void netShare(IView view, Integer targetUserId, int objectId, String type, int shareType, OnShareCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).sharefun(
                        AppDataHelper.getUser().getUserId(),
                        targetUserId, objectId, type, shareType),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    ////////////////////////////////////

    public interface OnShareCallback {
        void success();
    }
}
