package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-15 13:48
 * ============================================
 */
public interface GroupConversationContract {

    interface View extends IView {

        void showAllTeams(List<TeamInfo> teams);

        void showSelfTeams(List<TeamInfo> teams);
    }

    interface Model extends IModel {

        /**
         * 获取觅聊列表
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<ListDataDTO<TeamInfoDTO, TeamInfo>>> getTeamList(String socialId);

        /**
         * 获取觅聊列表
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<ListDataDTO<TeamInfoDTO, TeamInfo>>> getSelfTeamList(String socialId);
    }
}
