package com.wang.social.personal.mvp.presonter;

import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.helper.MyApiHelper;
import com.wang.social.personal.mvp.contract.LableContract;
import com.wang.social.personal.mvp.entities.lable.Lable;
import com.wang.social.personal.mvp.entities.lable.LableWrap;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/4/3.
 */

public class LablePresonter extends BasePresenter<LableContract.Model, LableContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    @Inject
    public LablePresonter(LableContract.Model model, LableContract.View view) {
        super(model, view);
    }

    public void getShowtag() {
        MyApiHelper.execute(mRootView, true,
                mModel.getShowtag(),
                new ErrorHandleSubscriber<BaseJson<LableWrap>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<LableWrap> basejson) {
                        LableWrap wrap = basejson.getData();
                        List<Lable> lables = wrap.getList();
                        mRootView.freshTagList(lables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public void getSelftags(int parentId) {
        MyApiHelper.execute(mRootView, true,
                mModel.getSelftags(parentId),
                new ErrorHandleSubscriber<BaseJson<LableWrap>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<LableWrap> basejson) {
                        LableWrap wrap = basejson.getData();
                        List<Lable> lables = wrap.getList();
                        mRootView.freshFlagList(lables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public void deltag(int tagId) {
        MyApiHelper.execute(mRootView, true,
                mModel.deltag(tagId),
                new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        ToastUtil.showToastLong(basejson.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public void getParentTags() {
        MyApiHelper.execute(mRootView, true,
                mModel.getParentTags(),
                new ErrorHandleSubscriber<BaseJson<LableWrap>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<LableWrap> basejson) {
                        LableWrap wrap = basejson.getData();
                        List<Lable> lables = wrap.getList();
                        mRootView.freshParentTagList(lables);
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