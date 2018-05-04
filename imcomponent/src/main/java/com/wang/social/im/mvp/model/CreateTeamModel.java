package com.wang.social.im.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.CreateTeamContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.TeamAttribute;
import com.wang.social.im.mvp.model.entities.dto.CreateGroupResultDTO;
import com.wang.social.im.mvp.model.entities.dto.PayCheckInfoDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-03 14:06
 * ============================================
 */
@ActivityScope
public class CreateTeamModel extends BaseModel implements CreateTeamContract.Model {

    @Inject
    public CreateTeamModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<PayCheckInfoDTO>> checkCreateTeamStatus(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .checkCreateTeamStatus("2.0.0", socialId);
    }

    @Override
    public Observable<BaseJson<CreateGroupResultDTO>> createTeam(String orderApplyId, String cover, String avatar, String name, String tag, TeamAttribute attr) {
        Map<String, Object> map = NetParam.newInstance()
                .put("v", "2.0.0")
                .put("applyId", orderApplyId)
                .put("groupName", name)
                .put("groupCoverPlan", cover)
                .put("headUrl", avatar)
                .put("isOpen", 1)
                .put("validation", 0)
                .put("isFree", attr.isCharge() ? 0 : 1)
                .put("gemstone", attr.getGem())
                .put("tagIds", tag)
                .build();
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .createTeam(map);
    }
}
