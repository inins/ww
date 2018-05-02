package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;

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
    }

    interface Model extends GroupContract.GroupModel {

        /**
         * 获取趣聊详情
         *
         * @param socialId
         * @return
         */
        Observable<BaseJson<SocialDTO>> getSocialInfo(String socialId);
    }
}
