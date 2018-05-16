package com.wang.social.im.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.component.entities.UserInfo;
import com.frame.component.entities.dto.UserInfoDTO;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.NobodyContract;
import com.wang.social.im.mvp.model.api.ChainService;
import com.wang.social.im.mvp.model.api.ImCommonService;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 9:38
 * ============================================
 */
@FragmentScope
public class NobodyModel extends BaseModel implements NobodyContract.Model {

    @Inject
    public NobodyModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getKnowledgeUser() {
        Map<String, Object> param = new NetParam()
                .put("size", 3)
                .put("current", 1)
                .put("from", "chat")
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(ImCommonService.class)
                .getKnowledgeUser(param);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getFunShowUser() {
        return mRepositoryManager
                .obtainRetrofitService(ImCommonService.class)
                .getFunShowUserList("chat", 1, 3);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getNewUsers() {
        return mRepositoryManager
                .obtainRetrofitService(ChainService.class)
                .getNewUsers("2.0.0");
    }
}
