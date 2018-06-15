package com.wang.social.im.mvp.model;

import com.frame.component.helper.AppDataHelper;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.CTSocialListContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;
import com.wang.social.im.mvp.model.entities.SimpleSocial;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.SimpleGroupInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.SimpleSocialDTO;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-14 10:10
 * ============================================
 */
@ActivityScope
public class CTSocialListModel extends BaseModel implements CTSocialListContract.Model {

    @Inject
    public CTSocialListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<SimpleSocialDTO, SimpleSocial>>> getSocials() {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getSocialList("2.0.0", AppDataHelper.getUser().getUserId(), 0);
    }
}
