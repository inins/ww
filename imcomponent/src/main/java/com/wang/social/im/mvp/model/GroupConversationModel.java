package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.GroupConversationContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 13:48
 * ============================================
 */
@ActivityScope
public class GroupConversationModel extends BaseModel implements GroupConversationContract.Model {

    @Inject
    public GroupConversationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<TeamInfoDTO, TeamInfo>>> getTeamList(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getTeamList("2.0.0", socialId, GroupService.TEAM_LIST_ALL);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<TeamInfoDTO, TeamInfo>>> getSelfTeamList(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getSelfTeamList("2.0.0", socialId);
    }
}
