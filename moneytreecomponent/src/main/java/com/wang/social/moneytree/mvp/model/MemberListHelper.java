package com.wang.social.moneytree.mvp.model;

import android.support.annotation.IntDef;

import com.frame.component.BuildConfig;
import com.frame.component.common.NetParam;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;
import com.wang.social.moneytree.mvp.model.api.MoneyTreeService;
import com.wang.social.moneytree.mvp.model.entities.MemberList;
import com.wang.social.moneytree.mvp.model.entities.dto.MemberListDTO;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MemberListHelper {
    public static MemberListHelper newInstance() {
        return new MemberListHelper();
    }

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;
    ApiHelper mApiHelper;

    private MemberListHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
        this.mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
    }

    /**
     * 游戏成员列表
     * @param gameId 游戏id
     */
    private Observable<BaseJson<MemberList>> getMemberList(int gameId) {
        Map<String, Object> param = new NetParam()
                .put("gameId", gameId)
                .put("v", "2.0.0")
                .putSignature()
                .build();

        return mRepositoryManager
                .obtainRetrofitService(MoneyTreeService.class)
                .getMemberList(param);

    }

    /**
     * 游戏成员列表
     */
    public void getMemberList(IView bindView,
                              int gameId,
                              ErrorHandleSubscriber<MemberList> errorHandleSubscriber, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        mApiHelper.executeForData(bindView,
                getMemberList(gameId),
                errorHandleSubscriber, doOnSubscribe, doFinally);
    }
}
