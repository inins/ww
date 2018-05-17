package com.wang.social.im.mvp.presenter;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.FunPointContract;

import javax.inject.Inject;

/**
 * ============================================
 * {@link com.wang.social.im.mvp.ui.TeamFunPointActivity}
 * <p>
 * Create by ChenJing on 2018-05-17 10:15
 * ============================================
 */
@ActivityScope
public class FunPointPresenter extends BasePresenter<FunPointContract.Model, FunPointContract.View> {

    private int mCurrentPage = 0;

    @Inject
    public FunPointPresenter(FunPointContract.Model model, FunPointContract.View view) {
        super(model, view);
    }

    private void getFunPoints(String teamId, boolean refresh){
        mCurrentPage++;
        ApiHelperEx.execute(mRootView, false, mModel.getFunPointList(teamId),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funpoint>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funpoint>> baseListWrapBaseJson) {
                        if (baseListWrapBaseJson.getData() != null && baseListWrapBaseJson.getData().getList() != null &&
                                baseListWrapBaseJson.getData().getList().size() > 0) {
                            mRootView.showFunPoint(baseListWrapBaseJson.getData().getList().get(0));
                        }
                    }
                });
    }
}