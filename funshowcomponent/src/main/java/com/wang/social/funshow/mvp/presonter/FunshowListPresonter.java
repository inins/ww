package com.wang.social.funshow.mvp.presonter;

import com.frame.component.entities.BaseListWrap;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.mvp.contract.FunshowListContract;
import com.wang.social.funshow.mvp.entities.Funshow;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/4/3.
 */
@FragmentScope
public class FunshowListPresonter extends BasePresenter<FunshowListContract.Model, FunshowListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    private int current = 1;
    private int size = 10;

    @Inject
    public FunshowListPresonter(FunshowListContract.Model model, FunshowListContract.View view) {
        super(model, view);
    }

    public void netGetFunshowList(int type) {
        current = 1;
        ApiHelperEx.execute(mRootView, false,
                mModel.getFunshowList(type, current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funshow>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funshow>> basejson) {
                        BaseListWrap<Funshow> warp = basejson.getData();
                        List<Funshow> list = warp.getList();
                        mRootView.reFreshList(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
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