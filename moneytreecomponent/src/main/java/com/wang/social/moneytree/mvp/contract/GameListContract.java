package com.wang.social.moneytree.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.moneytree.mvp.model.entities.NewGame;
import com.wang.social.moneytree.mvp.model.entities.dto.GameBeansDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.NewGameDTO;

import io.reactivex.Observable;

public interface GameListContract {
    interface View extends IView {
        void showToastLong(String msg);

        void onLoadGameListSuccess();
        void onLoadGameListCompleted();

        void onCreateGameSuccess(NewGame newGame);
        void onCreateGameCompleted();
    }


    interface Model extends IModel {
        /**
         * 游戏列表
         *
         * @param size    每页条数
         * @param current 页码
         */
        Observable<BaseJson<GameBeansDTO>> getMoneyTreeList(int size, int current);

        /**
         * 创建游戏
         *
         * @param groupId   群ID (在群中创建时必传)
         * @param type      创建类型（1：通过群，2：用户）
         * @param gameType  游戏类型（1：人数，2：时间）
         * @param resetTime 重置时长(s)
         * @param diamond   钻石数
         * @param peopleNum 开始人数 (gameType=1时必传)
         */
        Observable<BaseJson<NewGameDTO>> createGame(int groupId, int type, int gameType,
                                                    int resetTime, int diamond, int peopleNum);
    }
}
