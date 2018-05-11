package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.RxLifecycleUtils;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:02
 * ============================================
 */
@ActivityScope
public class MemberListPresenter extends BasePresenter<MemberListContract.Model, MemberListContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public MemberListPresenter(MemberListContract.Model model, MemberListContract.View view) {
        super(model, view);
    }

    public void getMembers(String groupId) {
        mModel.getMembers(groupId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .map(new Function<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>, ListData<MemberInfo>>() {
                    @Override
                    public ListData<MemberInfo> apply(BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>> t) throws Exception {
                        return t.getData().transform();
                    }
                })
                .map(new Function<ListData<MemberInfo>, List<MembersLevelOne>>() {
                    @Override
                    public List<MembersLevelOne> apply(ListData<MemberInfo> simpleGroupInfoListData) throws Exception {
                        //组装数据

                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<MembersLevelOne>>() {
                    @Override
                    public void onNext(List<MembersLevelOne> memberList) {
                        mRootView.showMembers(memberList);
                    }
                });
    }
}
