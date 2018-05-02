package com.wang.social.topic.mvp.model;

import com.frame.component.common.NetParam;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.BaseModel;
import com.wang.social.topic.BuildConfig;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;
import com.wang.social.topic.mvp.model.api.TopicService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

@ActivityScope
public class ReleaseTopicModel extends BaseModel implements ReleaseTopicContract.Model {

    @Inject
    public ReleaseTopicModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }


    /**
     * 话题发布
     *
     * @param title             话题标题
     * @param content           话题正文
     * @param firstStrff        展示在列表的第一段文字
     * @param tagIds            标签ID列表，以逗号隔开
     * @param relateState       收费状态 0不收费 1收费
     * @param gemstone          收费宝石
     * @param longitude         经度
     * @param latitude          纬度
     * @param adcode            区县编码
     * @param address           具体地址
     * @param templateId        主题模板ID
     * @param backgroundImage   话题背景图片URL
     * @param backgroundMusicId 背景音乐ID
     * @param informUserIds     @用户的ID列表，以逗号分隔
     * @return
     */
    @Override
    public Observable<BaseJson> addTopic(String title, String content, String firstStrff,
                                         String tagIds, int relateState, int gemstone,
                                         String longitude, String latitude, String adcode,
                                         String address, int templateId, String backgroundImage,
                                         int backgroundMusicId, String informUserIds) {
        Map<String, Object> param = new NetParam()
                .put("title", title)
                .put("content", content)
                .put("firstStrff", firstStrff)
                .put("tagIds", tagIds)
                .put("relateState", relateState)
                .put("gemstone", gemstone)
                .put("longitude", longitude)
                .put("latitude", latitude)
                .put("adcode", adcode)
                .put("address",address )
                .put("templateId", templateId)
                .put("backgroundImage", backgroundImage)
                .put("backgroundMusicId", backgroundMusicId)
                .put("informUserIds", informUserIds)
                .build();

        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .addTopic(param);
    }

    @Override
    public Observable<BaseJson> addTopic(Map<String, Object> param) {
        return mRepositoryManager
                .obtainRetrofitService(TopicService.class)
                .addTopic(param);
    }

}
