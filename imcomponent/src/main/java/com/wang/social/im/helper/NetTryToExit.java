package com.wang.social.im.helper;

import android.view.View;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.utils.FunShowUtil;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.TryToExit;
import com.wang.social.im.mvp.model.entities.dto.TryToExitDTO;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import io.reactivex.Observable;

public class NetTryToExit {

    ApiHelper mApiHelper;
    IRepositoryManager mRepositoryManager;

    private NetTryToExit() {
        this.mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
    }

    public static NetTryToExit newInstance() {
        return new NetTryToExit();
    }


    public void tryToExit(IView view, MemberInfo memberInfo, boolean selfQuit, OnTryToExitCallback callback) {
        mApiHelper.execute(view,
                netTryToExit(memberInfo.getGroupId(), memberInfo.getMemberId(), selfQuit),
                new ErrorHandleSubscriber<TryToExit>() {
                    @Override
                    public void onNext(TryToExit tryToExit) {
                        if (null != callback) {
                            callback.onTryToExit(memberInfo, tryToExit);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                },
                disposable -> view.showLoading(),
                () -> view.hideLoading());
    }

    private Observable<BaseJson<TryToExitDTO>> netTryToExit(String groupId, String memberId, boolean selfQuit) {
        Map<String, Object> param = new NetParam()
                .put("groupId", groupId)
                .put("userId", memberId)
                .put("state", selfQuit ? 1 : 0)
                .put("v", "2.0.2")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .tryToExit(param);
    }

    public interface OnTryToExitCallback {
        void onTryToExit(MemberInfo memberInfo, TryToExit result);
    }
}
