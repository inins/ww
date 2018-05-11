package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:01
 * ============================================
 */
@ActivityScope
public class MemberListModel extends BaseModel implements MemberListContract.Model {

    @Inject
    public MemberListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMembers(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getMembers("2.0.0", groupId);
    }

    @Override
    public Observable<BaseJson> kickOutMember(String groupId, String memberUid) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .kickOutMember("2.0.0", groupId, memberUid);
    }
}