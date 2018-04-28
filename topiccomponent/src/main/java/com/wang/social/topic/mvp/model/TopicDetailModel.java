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
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .getTopicDetails(param);
    }


    /**
     * 话题点赞/取消点赞
     * @param topicId 话题ID
     * @param type 类型1点赞 2取消点赞
     * @return
     */
    @Override
    public Observable<BaseJson> topicSupport(int topicId, String type) {
        NetParam netParam = new NetParam();

        netParam.put("topicId", topicId);
        netParam.put("type",type);
        netParam.put("v", "2.0.0");
        Map<String, Object> param = netParam.build();

        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .topicSupport(param);
    }
}
