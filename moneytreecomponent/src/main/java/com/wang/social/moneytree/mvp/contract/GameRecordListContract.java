package com.wang.social.moneytree.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.moneytree.mvp.model.entities.NewGame;
import com.wang.social.moneytree.mvp.model.entities.dto.GameBeansDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.GameRecordsDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.NewGameDTO;

import io.reactivex.Observable;

public interface GameRecordListContract {
    interface View extends IView {
        void onLoadRecordListSuccess();
        void onLoadRecordListCompleted();
    }


    interface Model extends IModel {
        /**
         * 游戏记录列表
         * @param size 每页条数
         * @param current 页码
         * @param type 输赢情况（默认：0:全部；1:赢;2:输;3:平）
         */
        Observable<BaseJson<GameRecordsDTO>> getRecordList(int size, int current, int type);
    }
}
