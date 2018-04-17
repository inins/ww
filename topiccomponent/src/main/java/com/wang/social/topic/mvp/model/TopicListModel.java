package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.BuildConfig;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.dto.TagsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;

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
    public Observable<BaseJson<TopicRspDTO>> getNewsList(String isCondition, int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("isCondition", isCondition)
                .put("size",size)
                .put("current",current)
                .put("v", BuildConfig.VERSION_NAME)
                .build();
        return mRepositoryManager
                .obtainCacheService(TopicService.class)
                .getNewsList(param);
    }

}
