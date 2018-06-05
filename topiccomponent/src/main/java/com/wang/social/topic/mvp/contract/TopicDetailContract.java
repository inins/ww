package com.wang.social.topic.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.TopicDetail;
import com.wang.social.topic.mvp.model.entities.dto.TopicDetailDTO;

import io.reactivex.Observable;

public interface TopicDetailContract {
    interface View extends IView {
        void onTopicDetailLoadSuccess(TopicDetail detail);
        void showToast(String msg);
        void resetSupportLayout(int isSupport, int count);
        void onDelMyTopicSuccess(int topicId);
    }


    interface Model extends IModel {
        /**
         * 话题详情
         * @param topicId 话题ID
         * @return
         */
        Observable<BaseJson<TopicDetailDTO>> getTopicDetails(int topicId);
        Observable<BaseJson> topicSupport(int topicId, String type);
        Observable<BaseJson> report(int objectId, String type, String comment, String picUrl);
        Observable<BaseJson> delMyTopic(int id);
    }
}
