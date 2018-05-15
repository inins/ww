package com.frame.component.ui.acticity.PersonalCard.contract;

import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserInfoDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.DTO.UserStatisticsDTO;
import com.frame.component.ui.acticity.PersonalCard.model.entities.PersonalInfo;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserStatistics;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;

import io.reactivex.Observable;

public interface PersonalCardContract {
    interface View extends IView {
        void onLoadUserInfoSuccess(PersonalInfo userInfo);
        void onLoadUserStatisticsSuccess(UserStatistics statistics);
    }

    interface Model extends IModel {
        /**
         * 用户数据统计（我的/推荐/个人名片）
         * @param userId 用户ID
         */
        Observable<BaseJson<UserStatisticsDTO>> getUserStatistics(int userId);

        /**
         * 用户信息加个人相册输出（我的/个人名片）
         * @param userId 用户ID,如果查询自己的名片信息不传
         */
        Observable<BaseJson<UserInfoDTO>> getUserInfoAndPhotos(int userId);
    }
}
