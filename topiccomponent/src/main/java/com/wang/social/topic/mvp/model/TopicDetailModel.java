package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.BuildConfig;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.api.TagService;
import com.wang.social.topic.mvp.model.api.TopicService;
import com.wang.social.topic.mvp.model.entities.dto.TopicDetailDTO;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class TopicDetailModel extends BaseModel implements TopicDetailContract.Model {

    @Inject
    public TopicDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<BaseJson<TopicDetailDTO>> getTopicDetails(int topicId) {
        Map<String, Object> param = new NetParam()
                .put("topicId",topicId)
                .put("v", BuildConfig.VERSION_NAME)
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .getTopicDetails(param);
    }
}
