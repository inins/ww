package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.BuildConfig;
import com.wang.social.topic.mvp.contract.SearchContract;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.dto.SearchResultsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class SearchModel extends BaseModel implements SearchContract.Model {

    @Inject
    public SearchModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<SearchResultsDTO>> searchTopic(String keyword, String tagNames, int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("keyword",keyword)
                .put("tagNames",tagNames)
                .put("size",size)
                .put("current",current)
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .searchTopic(param);
    }
}
