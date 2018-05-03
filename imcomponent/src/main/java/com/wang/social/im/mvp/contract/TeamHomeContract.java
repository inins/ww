package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 15:15
 * ============================================
 */
public interface TeamHomeContract {

    interface View extends GroupContract.GroupView {

        void showTeamInfo(TeamInfo teamInfo);
    }

    interface Model extends GroupContract.GroupModel {

        /**
         * 获取觅聊详情
         * @param teamId
         * @return
         */
        Observable<BaseJson<TeamInfoDTO>> getTeamInfo(String teamId);
    }
}
