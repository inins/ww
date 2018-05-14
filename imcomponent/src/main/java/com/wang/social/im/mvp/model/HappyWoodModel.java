package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.HappyWoodContract;
import com.wang.social.im.mvp.model.api.ShareWoodService;
import com.wang.social.im.mvp.model.entities.dto.ShareDTO;
import com.wang.social.im.mvp.model.entities.dto.ShareInfoDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-14 17:35
 * ============================================
 */
@ActivityScope
public class HappyWoodModel extends BaseModel implements HappyWoodContract.Model {

    @Inject
    public HappyWoodModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ShareDTO>> getTreeData(String type, String id) {
        return mRepositoryManager
                .obtainRetrofitService(ShareWoodService.class)
                .getTreeData("2.0.0", type, id);
    }

    @Override
    public Observable<BaseJson<ShareInfoDTO>> getShareInfo(String type, String id) {
        return mRepositoryManager
                .obtainRetrofitService(ShareWoodService.class)
                .getShareInfo("2.0.0", type, id);
    }
}
