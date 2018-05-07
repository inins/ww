package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialHomeDTO;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 16:44
 * ============================================
 */
public interface SocialHomeContract {

    interface View extends GroupContract.GroupView {

        /**
         * 显示趣聊信息
         *
         * @param socialInfo
         */
        void showSocialInfo(SocialInfo socialInfo);

        /**
         * 显示觅聊列表
         * @param teams
         */
        void showTeams(List<TeamInfo> teams);
    }

    interface Model extends GroupContract.GroupModel {

        /**
         * 获取趣聊详情
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<SocialHomeDTO>> getSocialInfo(String socialId);

        /**
         * 修改趣聊信息
         * @param social
         * @return
         */
        Observable<BaseJson> updateSocialInfo(SocialInfo social);

        /**
         * 获取觅聊列表
         * @param socialId
         * @return
         */
        Observable<BaseJson<ListDataDTO<TeamInfoDTO, TeamInfo>>> getTeamList(String socialId);
    }
}
