package com.frame.component.helper;

import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.entities.BillBoard;
import com.frame.component.entities.IsShoppingRsp;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetBillBoardHelper {

    private IRepositoryManager mRepositoryManager;
    private RxErrorHandler mErrorHandler;
    private ApiHelper mApiHelper;

    private NetBillBoardHelper() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
        this.mErrorHandler = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler();
        this.mApiHelper = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).apiHelper();
    }

    public static NetBillBoardHelper newInstance() {
        return new NetBillBoardHelper();
    }

    /**
     * 广告获取
     * @param view IView
     * @param callback 回调
     */
    public void getBillboard(IView view,
                               OnGetBillBoardCallback callback) {
        netGetBillboard(view, callback);
    }

    /**
     * 广告获取
     * @param view IView
     * @param callback 回调
     */
    private void netGetBillboard(IView view, OnGetBillBoardCallback callback) {
        Map<String, Object> param = new NetParam()
                .put("v", "2.0.0")
                .build();

        mApiHelper.execute(view,
                mRepositoryManager.obtainRetrofitService(CommonService.class)
                        .getBillboard(param),
                new ErrorHandleSubscriber<BillBoard>() {
                    @Override
                    public void onNext(BillBoard billBoard) {
                        if (null != callback) {
                            callback.onBillBoard(billBoard);
                        }
                    }
                });
    }

    public interface OnGetBillBoardCallback {
        void onBillBoard(BillBoard billBoard);
    }

    /**
     * 广告点击
     * @param view IView
     * @param billboardId 广告ID
     */
    public void clickBillBoard(IView view, int billboardId,
                               OnClickBillBoardCallback callback) {
        netClickBillboard(view, billboardId, callback);
    }

    /**
     * 广告点击
     * @param view IView
     * @param billboardId 广告ID
     */
    private void netClickBillboard(IView view, int billboardId,
                                   OnClickBillBoardCallback callback) {
        Map<String, Object> param = new NetParam()
                .put("billboardId", billboardId)
                .put("v", "2.0.0")
                .build();

        mApiHelper.executeForData(view,
                mRepositoryManager.obtainRetrofitService(CommonService.class)
                        .clickBillboard(param),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        if (null != callback) {
                            callback.onClickBillBoardResult(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != callback) {
                            callback.onClickBillBoardResult(false);
                        }
                    }
                });
    }



    public interface OnClickBillBoardCallback {
        void onClickBillBoardResult(boolean success);
    }

}
