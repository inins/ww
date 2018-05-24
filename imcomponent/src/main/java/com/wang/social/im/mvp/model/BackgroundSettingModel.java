package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.BackgroundSettingContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.OfficialImage;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.OfficialImageDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-24 9:17
 * ============================================
 */
@ActivityScope
public class BackgroundSettingModel extends BaseModel implements BackgroundSettingContract.Model {

    @Inject
    public BackgroundSettingModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<PageListDTO<OfficialImageDTO, OfficialImage>>> getOfficialImages(int currentPage) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getOfficialImages("2.0.0", 2, currentPage, 20);
    }
}