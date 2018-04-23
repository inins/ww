package com.wang.social.funshow.mvp.presonter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.mvp.contract.FunshowListContract;

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

    @Inject
    public FunshowListPresonter(FunshowListContract.Model model, FunshowListContract.View view) {
        super(model, view);
    }

    public void getShowtag() {
//        ApiHelperEx.execute(mRootView, false,
//                mModel.getShowtag(),
//                new ErrorHandleSubscriber<BaseJson<LableWrap>>(mErrorHandler) {
//                    @Override
//                    public void onNext(BaseJson<LableWrap> basejson) {
//                        LableWrap wrap = basejson.getData();
//                        List<Lable> lables = wrap.getList();
//                        mRootView.freshTagList(lables);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.showToastLong(e.getMessage());
//                    }
//                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }

}