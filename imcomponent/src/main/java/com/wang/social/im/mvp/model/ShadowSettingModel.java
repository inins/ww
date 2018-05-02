package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.ShadowSettingContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.dto.ShadowCheckInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 16:48
 * ============================================
 */
@ActivityScope
public class ShadowSettingModel extends BaseModel implements ShadowSettingContract.Model{

    @Inject
    public ShadowSettingModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ShadowCheckInfoDTO>> checkShadowStatus(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .checkShadowStatus("2.0.0", groupId);
    }

    @Override
    public Observable<BaseJson> updateShadowInfo(String socialId, String orderId, String nickname, String portrait) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .updateShadowInfo("2.0.0", socialId, orderId, nickname, portrait);
    }
}
