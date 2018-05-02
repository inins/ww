package com.wang.social.im.mvp.presenter;

import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.contract.GroupContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.MemberInfo;

import javax.inject.Inject;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 10:04
 * ============================================
 */
public class GroupPresenter<M extends GroupContract.GroupModel, V extends GroupContract.GroupView> extends BasePresenter<M, V> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    public GroupPresenter(M model, V view) {
        super(model, view);
    }

    /**
     * 获取成员列表
     *
     * @param groupId
     */
    public void getMemberList(String groupId) {
        mApiHelper.execute(mRootView, mModel.getMemberList(groupId),
                new ErrorHandleSubscriber<ListData<MemberInfo>>(mErrorHandler) {
                    @Override
                    public void onNext(ListData<MemberInfo> memberInfoListData) {
                        mRootView.showMembers(memberInfoListData.getList());
                    }
                });
    }
}