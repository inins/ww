package com.wang.social.im.mvp.model;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.InviteFriendContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.dto.IndexFriendInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 14:46
 * ============================================
 */
@ActivityScope
public class InviteFriendModel extends BaseModel implements InviteFriendContract.Model {

    @Inject
    public InviteFriendModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<IndexFriendInfoDTO, IndexFriendInfo>>> getInviteFriendList(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .inviteFriendList("2.0.0", socialId);
    }

    @Override
    public Observable<BaseJson> sendInvite(String socialId, List<IndexFriendInfo> friends) {
        StringBuilder builder = new StringBuilder();
        for (IndexFriendInfo friendInfo : friends) {
            builder.append(friendInfo.getFriendId()).append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .sendGroupInvite("2.0.0", socialId, builder.toString());
    }
}