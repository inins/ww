package com.frame.component.helper;

import android.view.View;

import com.frame.component.api.CommonService;
import com.frame.component.utils.FunShowUtil;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/4/4.
 * 点赞帮助类
 */

public class NetZanHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    private NetZanHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
    }

    public static NetZanHelper newInstance() {
        return new NetZanHelper();
    }

    //趣晒点赞
    public void funshowZan(IView view, View btn, int talkId, boolean isZan, OnZanCallback callback) {
        commonZan(view, btn,
                ApiHelperEx.getService(CommonService.class).funshowZan(talkId, isZan ? 1 : 2),
                isZan,
                callback);
    }

    //趣晒评论点赞
    public void funshowCommentZan(IView view, View btn, int talkId, int talkCommentId, boolean isZan, OnZanCallback callback) {
        commonZan(view, btn,
                ApiHelperEx.getService(CommonService.class).funshowCommentZan(talkId, talkCommentId, isZan ? 1 : 2),
                isZan,
                callback);
    }


    //话题点赞
    public void topicZan(IView view, View btn, int topicId, boolean isZan, OnZanCallback callback) {
        commonZan(view, btn,
                ApiHelperEx.getService(CommonService.class).topicZan(topicId, isZan ? 1 : 2),
                isZan,
                callback);
    }

    public void topicSupportWithNotification(IView view, int topicId, boolean isSupport) {
        commonZan(view, null,
                ApiHelperEx.getService(CommonService.class).topicZan(topicId, isSupport ? 1 : 2),
                isSupport,
                (boolean isZan, int zanCount) -> {
                    EventBean eventBean = new EventBean(EventBean.EVENTBUS_TOPIC_SUPPORT);
                    eventBean.put("topicId", topicId);
                    eventBean.put("isSupport", isZan);
                    EventBus.getDefault().post(eventBean);
                });
    }


    /**
     * @param view     绑定生命周期 可为null
     * @param btn      点赞btn（textView）.点赞成功后会自动+1或-1
     * @param call     请求
     * @param isZan    点赞还是取消赞
     * @param callback 回调（如果需要）
     */
    public void commonZan(IView view, View btn, Observable<BaseJson<Object>> call, boolean isZan, OnZanCallback callback) {
        ApiHelperEx.execute(view, false,
                call,
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        if (null != btn) {
                            btn.setSelected(isZan);
                        }
                        int num = FunShowUtil.addSubTextViewCount(btn, isZan);
                        if (callback != null) callback.onSuccess(isZan, num);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, () -> {
                    if (null != btn) {
                        btn.setEnabled(false);
                    }
                }, () -> {
                    if (null != btn) {
                        btn.setEnabled(true);
                    }
                });
    }

    public interface OnZanCallback {
        void onSuccess(boolean isZan, int zanCount);
    }
}
