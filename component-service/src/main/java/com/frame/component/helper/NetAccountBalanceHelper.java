package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.AccountBalance;
import com.frame.component.entities.dto.AccountBalanceDTO;
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

import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class NetAccountBalanceHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;
    ApiHelper mApiHelper;

    private NetAccountBalanceHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
        this.mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
    }

    public static NetAccountBalanceHelper newInstance() {
        return new NetAccountBalanceHelper();
    }

    public void accountBalance(IView view,
                               ErrorHandleSubscriber<AccountBalance> errorHandleSubscriber, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        Map<String, Object> param = new NetParam()
                .put("v", "2.0.0")
                .putSignature()
                .build();

        mApiHelper.execute(view,
                mRepositoryManager.obtainRetrofitService(CommonService.class)
                        .accountBalance(param),
                errorHandleSubscriber,
                doOnSubscribe, doFinally);
    }

    public void accountBalance(IView view, boolean needLoading, FindMyWalletCallback callback) {
        Map<String, Object> param = new NetParam()
                .put("v", "2.0.0")
                .putSignature()
                .build();


        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(CommonService.class)
                        .accountBalance(param),
                new ErrorHandleSubscriber<BaseJson<AccountBalanceDTO>>() {
                    @Override
                    public void onNext(BaseJson<AccountBalanceDTO> basejson) {
//                        Timber.i("钻石 " + basejson.getData().getDiamondNum());
                        if (null != callback) {
                            callback.onWallet(null != basejson && null != basejson.getData() ?
                                    (int) basejson.getData().getAmountDiamond() : 0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    public interface FindMyWalletCallback {
        void onWallet(int diamondNum);
    }
}
