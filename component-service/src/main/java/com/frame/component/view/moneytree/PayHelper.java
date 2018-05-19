package com.frame.component.view.moneytree;

import android.support.annotation.IntDef;

import com.frame.component.BuildConfig;
import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class PayHelper {
    public final static int PAY_TYPE_CREATE = 0;
    public final static int PAY_TYPE_JOIN = 1;

    @IntDef({
            PAY_TYPE_CREATE,
            PAY_TYPE_JOIN
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayTYpe {
    }

    public static PayHelper newInstance() {
        return new PayHelper();
    }

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;
    ApiHelper mApiHelper;

    private PayHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
        this.mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
    }

    /**
     * 游戏支付
     *
     * @param applyId     游戏申请ID
     * @param diamond     钻石数
     * @param payChannels 支付渠道(diamond)
     * @param gameType    游戏类型（0：创建 1：加入）
     *                    nonceStr	String	随机数
     *                    signature	String	签名
     *                    versionCode	String	版本号
     *                    channelCode	String	渠道号
     */
    private Observable<BaseJson> gamePay(int applyId, int diamond, String payChannels, int gameType) {
        Map<String, Object> param = new NetParam()
                .put("applyId", applyId)
                .put("diamond", diamond)
                .put("payChannels", payChannels)
                .put("gameType", gameType)
                .put("versionCode", BuildConfig.VERSION_CODE)
                .put("channelCode", "15")
                .put("v", "2.0.0")
                .putSignature()
                .build();

        return mRepositoryManager
                .obtainRetrofitService(CommonService.class)
                .gamePay(param);

    }

    /**
     * 钻石支付
     */
    private Observable<BaseJson> gamePay(int applyId, int diamond, int gameType) {
        return gamePay(applyId, diamond, "diamond", gameType);
    }

    /**
     * 创建游戏支付
     */
    public void payCreateGame(IView bindView,
                              int applyId, int diamond,
                              ErrorHandleSubscriber<Object> errorHandleSubscriber, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        mApiHelper.executeForData(bindView,
                gamePay(applyId, diamond, PAY_TYPE_CREATE),
                errorHandleSubscriber, doOnSubscribe, doFinally);
    }

    /**
     * 加入游戏支付
     */
    public void payJoinGame(IView bindView,
                            int applyId, int diamond,
                            ErrorHandleSubscriber<Object> errorHandleSubscriber, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        mApiHelper.executeForData(bindView,
                gamePay(applyId, diamond, PAY_TYPE_JOIN),
                errorHandleSubscriber, doOnSubscribe, doFinally);
    }
}
