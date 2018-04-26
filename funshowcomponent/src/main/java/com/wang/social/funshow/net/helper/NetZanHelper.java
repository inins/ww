package com.wang.social.funshow.net.helper;

import android.view.View;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.User;
import com.frame.component.entities.UserWrap;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.RegexUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.funshow.mvp.entities.user.TopUser;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.utils.FunShowUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    public void funshowZan(IView view, View btn, int talkId, boolean isZan, OnFunshowZanCallback callback) {
//        ApiHelperEx.execute(view, false,
//                ApiHelperEx.getService(FunshowService.class).funshowZan(talkId, isZan ? 1 : 2),
//                new ErrorHandleSubscriber<BaseJson<Object>>() {
//                    @Override
//                    public void onNext(BaseJson<Object> basejson) {
//                        btn.setSelected(isZan);
//                        int num = 0;
//                        if (btn instanceof TextView) {
//                            String numStr = ((TextView) btn).getText().toString();
//                            if (RegexUtils.isIntegerWhithZero(numStr)) {
//                                num = Integer.parseInt(numStr);
//                                ((TextView) btn).setText(String.valueOf(isZan ? ++num : --num));
//                            }
//                        }
//                        if (callback != null) callback.onSuccess(isZan, num);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.showToastLong(e.getMessage());
//                    }
//                }, () -> {
//                    btn.setEnabled(false);
//                }, () -> {
//                    btn.setEnabled(true);
//                });
        commonZan(view, btn,
                ApiHelperEx.getService(FunshowService.class).funshowZan(talkId, isZan ? 1 : 2),
                isZan,
                callback);
    }

    public void funshowCommentZan(IView view, View btn, int talkId, int talkCommentId, boolean isZan, OnFunshowZanCallback callback) {
        commonZan(view, btn,
                ApiHelperEx.getService(FunshowService.class).funshowCommentZan(talkId, talkCommentId, isZan ? 1 : 2),
                isZan,
                callback);
    }


    public void commonZan(IView view, View btn, Observable<BaseJson<Object>> call, boolean isZan, OnFunshowZanCallback callback) {
        ApiHelperEx.execute(view, false,
                call,
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        btn.setSelected(isZan);
//                        int num = 0;
//                        if (btn instanceof TextView) {
//                            String numStr = ((TextView) btn).getText().toString();
//                            if (RegexUtils.isIntegerWhithZero(numStr)) {
//                                num = Integer.parseInt(numStr);
//                                ((TextView) btn).setText(String.valueOf(isZan ? ++num : --num));
//                            }
//                        }
                        int num = FunShowUtil.addSubTextViewCount(btn, isZan);
                        if (callback != null) callback.onSuccess(isZan, num);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, () -> {
                    btn.setEnabled(false);
                }, () -> {
                    btn.setEnabled(true);
                });
    }

    public interface OnFunshowZanCallback {
        void onSuccess(boolean isZan, int zanCount);
    }
}
