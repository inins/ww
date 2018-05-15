package com.wang.social.im.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.wang.social.im.mvp.contract.TeamHomeContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamHomeDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:15
 * ============================================
 */
@ActivityScope
public class TeamHomeModel extends GroupModel implements TeamHomeContract.Model {

    @Inject
    public TeamHomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<TeamHomeDTO>> getSelfProfile(String teamId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getTeamHomeInfo("2.0.0", teamId);
    }

    @Override
    public Observable<BaseJson<TeamInfoDTO>> getTeamInfo(String teamId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getTeamInfo("2.0.0", teamId);
    }

    @Override
    public Observable<BaseJson> updateTeamInfo(TeamInfo teamInfo) {
        Map<String, Object> params = NetParam.newInstance()
                .put("v", "2.0.0")
                .put("groupId", teamInfo.getTeamId())
                .put("groupName", teamInfo.getName())
                .put("groupDesc", " ")
                .put("groupCoverPlan", teamInfo.getCover())
                .put("headUrl", teamInfo.getCover())
                .put("isFree", teamInfo.isFree() ? 1 : 0)
                .put("gemstone", teamInfo.getJoinCost())
                .put("validation", teamInfo.isValidation() ? 1 : 0)
                .build();
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .updateTeamInfo(params);
    }
}