package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;

import java.util.List;

import io.reactivex.Observable;


/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:00
 * ============================================
 */
public interface MemberListContract {

    interface View extends IView {

        void showMembers(List<MembersLevelOne> members);
    }

    interface Model extends IModel {

        /**
         * 获取成员列表
         *
         * @param groupId
         * @return
         */
        Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMembers(String groupId);
    }
}