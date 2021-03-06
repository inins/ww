package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.component.entities.Topic;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.dto.TopicDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@FragmentScope
public class TopicListModel extends BaseModel implements TopicListContract.Model {

    @Inject
    public TopicListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getNewsList(String isCondition, int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("isCondition", isCondition)
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .getNewsList(param);
    }

    @Override
    public Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getHotList(int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .getHotList(param);
    }

    @Override
    public Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> getLowList(int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .getLowList(param);
    }

}
