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

public class NetReadHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    private NetReadHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
    }

    public static NetReadHelper newInstance() {
        return new NetReadHelper();
    }

    /**
     * 阅读趣点
     */
    public void netReadFunpoint(int newsId, OnReadCallback callback) {
        ApiHelperEx.execute(null, false,
                ApiHelperEx.getService(CommonService.class).readFunpoint(newsId),
                new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    ////////////////////////////////////

    public interface OnReadCallback {
        void success();
    }
}
