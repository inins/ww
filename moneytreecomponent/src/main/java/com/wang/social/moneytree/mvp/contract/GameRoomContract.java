package com.wang.social.moneytree.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.model.entities.GameRecordDetail;
import com.wang.social.moneytree.mvp.model.entities.dto.GameEndDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.GameRecordDetailDTO;
import com.wang.social.moneytree.mvp.model.entities.JoinGame;
import com.wang.social.moneytree.mvp.model.entities.RoomMsg;
import com.wang.social.moneytree.mvp.model.entities.dto.JoinGameDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.RoomMsgDTO;

import io.reactivex.Observable;

public interface GameRoomContract {
    interface View extends IView {
        void showToastLong(String msg);

        void onGetRoomMsgSuccess(RoomMsg roomMsg);

        void popupJoinGamePayDialog(JoinGame joinGame);
        void onPayJoinGameSuccess();

        void onLoadMemberListSuccess();

        void onGetRecordDetailSuccess(GameRecordDetail recordDetail);
        void onLoadGameEndSuccess(GameEnd gameEnd);
    }


    interface Model extends IModel {
        /**
         * 获取游戏房间展示项
         * @param roomId 房间id
         */
        Observable<BaseJson<RoomMsgDTO>> getRoomMsg(int roomId);

        /**
         * 加入游戏
         * @param roomId 房间id
         */
        Observable<BaseJson<JoinGameDTO>> joinGame(int roomId);

        /**
         * 游戏记录详情
         * @param gameId 游戏id
         */
        Observable<BaseJson<GameRecordDetailDTO>> getRecordDetail(int gameId);

        /**
         * 游戏结果
         * @param gameId 游戏id
         * @return
         */
        Observable<BaseJson<GameEndDTO>> getGameEnd(int gameId);
    }
}
