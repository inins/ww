package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.AlertUserListContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexMemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:38
 * ============================================
 */
@ActivityScope
public class AlertUserListModel extends BaseModel implements AlertUserListContract.Model {

    @Inject
    public AlertUserListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<IndexMemberInfoDTO, IndexMemberInfo>>> getAlertMembers(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getAlertMembers("2.0.0", groupId);
    }
}