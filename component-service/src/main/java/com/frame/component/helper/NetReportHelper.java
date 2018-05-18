package com.frame.component.helper;

import android.app.FragmentManager;
import android.text.TextUtils;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.http.Field;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetReportHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;
    QiNiuManager mQiNiuManager;

    private NetReportHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
        this.mQiNiuManager = new QiNiuManager(mRepositoryManager);
    }

    public static NetReportHelper newInstance() {
        return new NetReportHelper();
    }

    /**
     * 举报趣晒
     */
    public void netReportFunshow(IView view, int talkId, OnReportCallback callback) {
        netReport(view, talkId, 2, null, null, callback);
    }

    /**
     * 举报用户
     */
    public void netReportPerson(IView view, int objectId, String comment, String picUrl, OnReportCallback callback) {
        doNetReport(view, objectId, 0, comment, picUrl, callback);
    }

    public void netReportPersonWithUpload(IView view, int objectId, String comment, String[] picUrls, OnReportCallback callback) {
        netReport(view, objectId, 0, comment, picUrls, callback);
    }

    public void netReportGroupWithUpload(IView view, int objectId, String comment, String[] picUrls, OnReportCallback callback) {
        netReport(view, objectId, 1, comment, picUrls, callback);
    }

    private void netReport(IView view, int objectId, int type, String comment, String[] picUrls, OnReportCallback callback) {
        // 需要上传图片的话 先上传图片
        if (null != picUrls && picUrls.length > 0) {
            ArrayList<String> list = new ArrayList<>(Arrays.asList(picUrls));

            mQiNiuManager.uploadFiles(view, list, new QiNiuManager.OnBatchUploadListener() {
                @Override
                public void onSuccess(ArrayList<String> urls) {
                    String picUrl = "";
                    for (int i = 0; i < urls.size(); i++) {
                        picUrl += (i <= 0 ? "" : ",") + urls.get(i);
                    }

                    doNetReport(view, objectId, type, comment, picUrl, callback);
                }

                @Override
                public void onFail() {
                    ToastUtil.showToastShort("上传图片失败");
                }
            });
        } else {
            doNetReport(view, objectId, type, comment, null, callback);
        }
    }

    private void doNetReport(IView view, int objectId, int type, String comment, String picUrl, OnReportCallback callback) {
        ApiHelperEx.execute(view, true,
                ApiHelperEx.getService(CommonService.class).report(objectId, type, comment, picUrl),
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
