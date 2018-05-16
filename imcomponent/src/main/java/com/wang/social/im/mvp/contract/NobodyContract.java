package com.wang.social.im.mvp.contract;

import com.frame.component.entities.UserInfo;
import com.frame.component.entities.dto.UserInfoDTO;
import com.frame.component.view.barview.BarUser;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 9:37
 * ============================================
 */
public interface NobodyContract {

    interface View extends IView {

        void showKnowledgeUsers(List<BarUser> users);

        void showFunShowUsers(List<BarUser> users);

        void showNewUsers(List<BarUser> users);
    }

    interface Model extends IModel {

        /**
         * 获取知识魔用户
         *
         * @return
         */
        Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getKnowledgeUser();

        /**
         * 获取趣晒魔用户
         *
         * @return
         */
        Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getFunShowUser();

        /**
         * 获取新用户
         * @return
         */
        Observable<BaseJson<ListDataDTO<UserInfoDTO, UserInfo>>> getNewUsers();
    }
}
