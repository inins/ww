package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
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

import retrofit2.http.Field;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetReportHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    private NetReportHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
    }

    public static NetReportHelper newInstance() {
        return new NetReportHelper();
    }

    /**
     * 举报趣晒
     */
    public void netReportFunshow(IView view, int talkId, OnReportCallback callback) {
        netReport(view, talkId, 2, callback);
    }

    private void netReport(IView view, int objectId, int type, OnReportCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).report(objectId, type),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        ToastUtil.showToastShort("举报成功");
                        if (callback != null) callback.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    ////////////////////////////////////

    public interface OnReportCallback {
        void success();
    }
}
