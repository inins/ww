package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.BuildConfig;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.api.TagService;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.dto.TagsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicTopUsersDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@FragmentScope
public class TopicModel extends BaseModel implements TopicContract.Model {

    @Inject
    public TopicModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<TopicTopUsersDTO>> getReleaseTopicTopUser(int size, int current, String from) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("from", from)
                .put("v", BuildConfig.VERSION_NAME)
                .build();

        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .getReleaseTopicTopUser(param);
    }

    @Override
    public Observable<BaseJson<TagsDTO>> myRecommendTag(int size, int current) {
        Map<String, Object> param = new NetParam()
                .put("size",size)
                .put("current",current)
                .put("v", BuildConfig.VERSION_NAME)
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TagService.class)
                .myRecommendTag(param);
    }

}
