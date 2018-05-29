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
        netShare(view, targetUserId, talkId, "talk", callback);
    }

    /**
     * 分享话题
     * @param view IView
     * @param targetUserId 目标用户id
     * @param topicId 话题id
     * @param callback 回调
     */
    public void netShareTopic(IView view, Integer targetUserId, int topicId, OnShareCallback callback) {
        netShare(view, targetUserId, topicId, "topic", callback);
    }

    private void netShare(IView view, Integer targetUserId, int objectId, String type, OnShareCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).sharefun(targetUserId, objectId, type),
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
