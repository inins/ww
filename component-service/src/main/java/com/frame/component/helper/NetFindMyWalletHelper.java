package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.DiamondNum;
import com.frame.component.entities.User;
import com.frame.component.entities.UserWrap;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetFindMyWalletHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    private NetFindMyWalletHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
    }

    public static NetFindMyWalletHelper newInstance() {
        return new NetFindMyWalletHelper();
    }

    public void findMyWallet(IView view, boolean needLoading, FindMyWalletCallback callback) {
//        Timber.i("findMyWallet");
        Map<String, Object> param = new NetParam()
                .put("v", "2.0.0")
                .putSignature()
                .build();


        ApiHelperEx.execute(view, needLoading,
                ApiHelperEx.getService(CommonService.class)
                        .findMyWallet(param),
                new ErrorHandleSubscriber<BaseJson<DiamondNum>>() {
                    @Override
                    public void onNext(BaseJson<DiamondNum> basejson) {
//                        Timber.i("钻石 " + basejson.getData().getDiamondNum());
                        if (null != callback) {
                            callback.onWallet(null != basejson && null != basejson.getData() ?
                                    basejson.getData().getDiamondNum() : 0);
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
