package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.CreateGroupResult;
import com.wang.social.im.mvp.model.entities.TeamAttribute;
import com.wang.social.im.mvp.model.entities.dto.CreateGroupResultDTO;
import com.wang.social.im.mvp.model.entities.dto.PayCheckInfoDTO;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-03 14:04
 * ============================================
 */
public interface CreateTeamContract {

    interface View extends IView {

        /**
         * 创建完成
         * @param result
         */
        void onCreateComplete(CreateGroupResult result);
    }

    interface Model extends IModel {

        /**
         * 检查创建觅聊是否需要支付
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<PayCheckInfoDTO>> checkCreateTeamStatus(String socialId, String tagId);

        /**
         * 创建觅聊
         *
         * @param cover
         * @param avatar
         * @param name
         * @param tag
         * @param attr
         * @return
         */
        Observable<BaseJson<CreateGroupResultDTO>> createTeam(String orderApplyId, String cover, String avatar, String name, String tag, TeamAttribute attr);
    }
}