package com.wang.social.funpoint.mvp.presonter;


import com.frame.component.entities.BaseListWrap;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funpoint.mvp.contract.FunpointListContract;
import com.wang.social.funpoint.mvp.entities.Funpoint;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/4/3.
 */
@FragmentScope
public class FunpointListPresonter extends BasePresenter<FunpointListContract.Model, FunpointListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private int current = 1;
    private int size = 10;

    @Inject
    public FunpointListPresonter(FunpointListContract.Model model, FunpointListContract.View view) {
        super(model, view);
    }

    public void netGetFunpointList(boolean needloading) {
        current = 1;
        ApiHelperEx.execute(mRootView, needloading,
                mModel.getFunpointList(1, current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funpoint>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funpoint>> basejson) {
                        BaseListWrap<Funpoint> warp = basejson.getData();
                        List<Funpoint> list = warp.getList();
                        mRootView.reFreshList(list);
                        mRootView.finishSpringView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        mRootView.finishSpringView();
                    }
                });
    }

    public void netLoadmore(int isCondition) {
        ApiHelperEx.execute(mRootView, false,
                mModel.getFunpointList(isCondition, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funpoint>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funpoint>> basejson) {
                        BaseListWrap<Funpoint> warp = basejson.getData();
                        List<Funpoint> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            mRootView.appendList(list);
                            mRootView.finishSpringView();
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                            mRootView.finishSpringView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        mRootView.finishSpringView();
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }

}