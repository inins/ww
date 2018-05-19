package com.wang.social.funshow.mvp.presonter;


import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.AppDataHelper;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.mvp.contract.FunshowListContract;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.user.TopUser;

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

    public void netGetFunshowList(int type, boolean needloading) {
        current = 1;
        ApiHelperEx.execute(mRootView, needloading,
                mModel.getFunshowList(type, current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funshow>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funshow>> basejson) {
                        BaseListWrap<Funshow> warp = basejson.getData();
                        List<FunshowBean> list = Funshow.tans2FunshowBeanList(warp.getList());
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

    public void netLoadmore(int type) {
        ApiHelperEx.execute(mRootView, false,
                mModel.getFunshowList(type, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funshow>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funshow>> basejson) {
                        BaseListWrap<Funshow> warp = basejson.getData();
//                        List<Funshow> list = warp.getList();
                        List<FunshowBean> list = Funshow.tans2FunshowBeanList(warp.getList());
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


    public void netGetFunshowTopUserList() {
        ApiHelperEx.execute(mRootView, false,
                mModel.getFunshowTopUserList("square", 1, 5),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<TopUser>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<TopUser>> basejson) {
                        BaseListWrap<TopUser> warp = basejson.getData();
                        List<TopUser> list = warp.getList();
                        mRootView.reFreshTopUsers(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public void shatDownUser(int userId) {
        if (userId == AppDataHelper.getUser().getUserId()) {
            ToastUtil.showToastShort("不能屏蔽自己");
            return;
        }
        ApiHelperEx.execute(mRootView, true,
                mModel.shatDownUser(userId + "", 1),
                new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        mRootView.callRefresh();
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