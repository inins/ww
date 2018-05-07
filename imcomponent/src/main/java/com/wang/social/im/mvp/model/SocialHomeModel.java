package com.wang.social.im.mvp.model;

import android.text.TextUtils;

import com.frame.component.ui.acticity.tags.Tag;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.SocialHomeContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialHomeDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 16:45
 * ============================================
 */
@ActivityScope
public class SocialHomeModel extends GroupModel implements SocialHomeContract.Model {

    @Inject
    public SocialHomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<SocialHomeDTO>> getSocialInfo(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getSocialHomeInfo("2.0.0", socialId);
    }

    @Override
    public Observable<BaseJson> updateSocialInfo(SocialInfo social) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("v", "2.0.0");
        params.put("groupName", social.getName());
        params.put("groupDesc", social.getDesc());
        params.put("groupCoverPlan", social.getCover());
        params.put("headUrl", social.getCover());
        params.put("isOpen", social.getAttr().isOpen() ? 1 : 0);
        params.put("isFree", social.getAttr().isCharge() ? 0 : 1);
        params.put("totalDiamond", social.getAttr().getGem());
        switch (social.getAttr().getGenderLimit()) {
            case MALE:
                params.put("gender", 0);
                break;
            case FEMALE:
                params.put("gender", 1);
                break;
        }
        if (!TextUtils.isEmpty(social.getAgeLimit())) {
            params.put("ageRange", social.getAgeLimit());
        }
        params.put("isCreateMi", social.isCreateTeam() ? 1 : 2);
        StringBuilder tagBuilder = new StringBuilder();
        for (Tag tag : social.getTags()) {
            tagBuilder.append(tag.getId())
                    .append(",");
        }
        if (tagBuilder.length() > 0) {
            tagBuilder.deleteCharAt(tagBuilder.length() - 1);
        }
        params.put("tagIds", tagBuilder.toString());
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .updateSocialInfo(params);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<TeamInfoDTO, TeamInfo>>> getTeamList(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getTeamList("2.0.0", socialId, GroupService.TEAM_LIST_ALL);
    }
}