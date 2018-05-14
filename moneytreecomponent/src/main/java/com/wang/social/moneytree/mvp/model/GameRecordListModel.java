package com.wang.social.moneytree.mvp.model;


import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.moneytree.mvp.contract.GameRecordListContract;
import com.wang.social.moneytree.mvp.model.api.MoneyTreeService;
import com.wang.social.moneytree.mvp.model.entities.dto.GameRecordsDTO;
import com.wang.social.moneytree.mvp.model.entities.dto.NewGameDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class GameRecordListModel extends BaseModel implements GameRecordListContract.Model {

    @Inject
    public GameRecordListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    /**
     * 游戏记录列表
     * @param size 每页条数
     * @param current 页码
     * @param type 输赢情况（默认：0:全部；1:赢;2:输;3:平）
     */
    @Override
    public Observable<BaseJson<GameRecordsDTO>> getRecordList(int size, int current, int type) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("type",type)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(MoneyTreeService.class)
                .getRecordList(param);
    }
}
