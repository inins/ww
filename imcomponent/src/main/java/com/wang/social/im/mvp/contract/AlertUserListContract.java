package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexMemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 17:20
 * ============================================
 */
public interface AlertUserListContract {

    interface View extends IView {

        void showMembers(List<IndexMemberInfo> members);
    }

    interface Model extends IModel {

        Observable<BaseJson<ListDataDTO<IndexMemberInfoDTO, IndexMemberInfo>>> getAlertMembers(String groupId);
    }
}