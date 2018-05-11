package com.frame.component.ui.acticity.PersonalCard.model;

import com.frame.component.common.NetParam;
import com.frame.component.ui.acticity.PersonalCard.contract.PersonalCardContract;
import com.frame.component.ui.acticity.PersonalCard.model.api.PersonalCardService;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class PersonalCardModel extends BaseModel implements PersonalCardContract.Model {

    @Inject
    public PersonalCardModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 用户数据统计（我的/推荐/个人名片）
     * @param userId 用户ID
     */
    @Override
    public Observable<BaseJson<UserStatisticsDTO>> getUserStatistics(int userId) {
        Map<String, Object> param = new NetParam()
                .put("userId", userId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .getUserStatistics(param);
    }

    /**
     * 用户信息加个人相册输出（我的/个人名片）
     * @param userId 用户ID,如果查询自己的名片信息不传
     */
    @Override
    public Observable<BaseJson<UserInfoDTO>> getUserInfoAndPhotos(int userId) {
        Map<String, Object> param = new NetParam()
                .put("userId", userId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(PersonalCardService.class)
                .getUserInfoAndPhotos(param);
    }


}
