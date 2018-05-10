package com.wang.social.moneytree.mvp.model;


import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.moneytree.mvp.contract.GameRoomContract;
import com.wang.social.moneytree.mvp.model.api.MoneyTreeService;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.model.entities.dto.GameEndDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.GameRecordDetailDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.JoinGameDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.RoomMsgDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class GameRoomModel extends BaseModel implements GameRoomContract.Model {

    @Inject
    public GameRoomModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 获取游戏房间展示项
     * @param roomId 房间id
     */
    @Override
    public Observable<BaseJson<RoomMsgDTO>> getRoomMsg(int roomId) {
        Map<String, Object> param = new NetParam()
                .put("roomId",roomId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(MoneyTreeService.class)
                .getRoomMsg(param);
    }

    /**
     * 加入游戏
     * @param roomId 房间id
     */
    @Override
    public Observable<BaseJson<JoinGameDTO>> joinGame(int roomId) {
        Map<String, Object> param = new NetParam()
            .put("roomId",roomId)
            .put("v", "2.0.0")
            .build();
        return mRepositoryManager
                .obtainRetrofitService(MoneyTreeService.class)
                .joinGame(param);
    }

    @Override
    public Observable<BaseJson<GameRecordDetailDTO>> getRecordDetail(int gameId) {
        Map<String, Object> param = new NetParam()
                .put("gameId", gameId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(MoneyTreeService.class)
                .getRecordDetail(param);
    }

    @Override
    public Observable<BaseJson<GameEndDTO>> getGameEnd(int gameId) {
        Map<String, Object> param = new NetParam()
                .put("gameId", gameId)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(MoneyTreeService.class)
                .getGameEnd(param);
    }


}
