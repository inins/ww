package com.wang.social.topic.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.TopicDetail;
import com.wang.social.topic.mvp.model.entities.dto.TopicDetailDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;

public interface TopicDetailContract {
    interface View extends IView {
        void onTopicDetailLoadSuccess(TopicDetail detail);
        void showToast(String msg);
    }


    interface Model extends IModel {
        /**
         * 话题详情
         * @param topicId 话题ID
         * @return
         */
        Observable<BaseJson<TopicDetailDTO>> getTopicDetails(int topicId);
    }
}
