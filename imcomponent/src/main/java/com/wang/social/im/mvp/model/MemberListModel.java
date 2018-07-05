package com.wang.social.im.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.TryToExitDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:01
 * ============================================
 */
@ActivityScope
public class MemberListModel extends BaseModel implements MemberListContract.Model {

    @Inject
    public MemberListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>> getMembers(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getMembers("2.0.0", groupId);
    }

    @Override
    public Observable<BaseJson> kickOutMember(String groupId, String memberUid) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .kickOutMember("2.0.0", groupId, memberUid);
    }

    /**
     * 尝试退出
     * @param groupId 趣聊/觅聊群id
     * @param memberId 退出用户id
     * @param selfQuit 是否自己退出( 0群主踢人，1用户自己退出)
     */
    @Override
    public Observable<BaseJson<TryToExitDTO>> tryToExit(String groupId, String memberId, boolean selfQuit) {
        Map<String, Object> param = new NetParam()
                .put("groupId", groupId)
                .put("userId", memberId)
                .put("state", selfQuit ? 1 : 0)
                .put("v", "2.0.2")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .tryToExit(param);
    }
}