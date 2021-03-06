package com.wang.social.im.mvp.model;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.ConversationContract;
import com.wang.social.im.mvp.model.api.EnvelopService;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.dto.AnonymousInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.EnvelopInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.GroupGameCheckResultDTO;
import com.wang.social.im.mvp.model.entities.dto.ShadowInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ======================================
 * <p>
 * Create by ChenJing on 2018-04-03 16:43
 * ======================================
 */
@FragmentScope
public class ConversationModel extends BaseModel implements ConversationContract.Model {

    @Inject
    public ConversationModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<EnvelopInfoDTO>> getEnvelopInfo(long envelopId) {
        return mRepositoryManager
                .obtainRetrofitService(EnvelopService.class)
                .getEnvelopInfo("2.0.0", envelopId);
    }

    @Override
    public Observable<BaseJson<BaseListWrap<Funpoint>>> getFunPointList(String teamId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getFunPointList(teamId, 1, 1);
    }

    @Override
    public Observable<BaseJson<ShadowInfoDTO>> getShadowInfo(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getShadowInfo("2.0.0", socialId);
    }

    @Override
    public Observable<BaseJson<AnonymousInfoDTO>> getAnonymousInfo() {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getAnonymousInfo("2.0.0");
    }

    @Override
    public Observable<BaseJson> updateShadowStatus(String socialId, boolean isOpen) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .updateShadowStatus("2.0.0", socialId, isOpen ? GroupService.SHADOW_STATE_OPEN : GroupService.SHADOW_STATE_CLOSE);
    }

    @Override
    public Observable<BaseJson<GroupGameCheckResultDTO>> checkHasGame(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .checkHasGame("2.0.0", socialId);
    }

    @Override
    public Observable<BaseJson<TeamInfoDTO>> getTeamInfo(String teamId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getTeamInfo("2.0.0", teamId);
    }
}