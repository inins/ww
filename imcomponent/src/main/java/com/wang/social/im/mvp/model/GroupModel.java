package com.wang.social.im.mvp.model;

import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.enums.MessageNotifyType;
import com.wang.social.im.mvp.contract.GroupContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;

import io.reactivex.Observable;

/**
 * ============================================
 * 趣聊/觅聊相关公共获取数据方法
 * <p>
 * Create by ChenJing on 2018-05-02 10:00
 * ============================================
 */
public class GroupModel extends BaseModel implements GroupContract.GroupModel {

    public GroupModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMemberList(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getMembers("2.0.0", groupId);
    }

    @Override
    public Observable<BaseJson> updateNameCard(String groupId, String nickname, String portrait, MessageNotifyType notifyType) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .updateNameCard("2.0.0", groupId, nickname, portrait, notifyType == MessageNotifyType.ALL ? 0 : 1);
    }

    @Override
    public Observable<BaseJson> dissolveGroup(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .dissolveGroup("2.0.0", groupId);
    }

    @Override
    public Observable<BaseJson> exitGroup(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .exitGroup("2.0.0", groupId);
    }
}