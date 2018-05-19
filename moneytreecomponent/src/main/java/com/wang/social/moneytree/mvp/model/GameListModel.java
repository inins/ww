package com.wang.social.moneytree.mvp.model;


import com.frame.component.common.NetParam;
import com.frame.component.entities.NewMoneyTreeGame;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.moneytree.mvp.contract.GameListContract;
import com.wang.social.moneytree.mvp.model.api.MoneyTreeService;
import com.wang.social.moneytree.mvp.model.entities.dto.GameBeansDTO;
import com.frame.component.entities.dto.NewMoneyTreeGameDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityScope
public class GameListModel extends BaseModel implements GameListContract.Model {

    @Inject
    public GameListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


    /**
     * 游戏列表
     * @param size 每页条数
     * @param current 页码
     */
    @Override
    public Observable<BaseJson<GameBeansDTO>> getMoneyTreeList(int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(MoneyTreeService.class)
                .getMoneyTreeList(param);
    }
}
