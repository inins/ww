package com.wang.social.moneytree.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.frame.component.entities.NewMoneyTreeGame;
import com.wang.social.moneytree.mvp.model.entities.dto.GameBeansDTO;
import com.frame.component.entities.dto.NewMoneyTreeGameDTO;

import io.reactivex.Observable;

public interface GameListContract {
    interface View extends IView {
        void showToastShort(String msg);

        void onLoadGameListSuccess();
        void onLoadGameListCompleted();
    }


    interface Model extends IModel {
        /**
         * 游戏列表
         *
         * @param size    每页条数
         * @param current 页码
         */
        Observable<BaseJson<GameBeansDTO>> getMoneyTreeList(int size, int current);
    }
}
