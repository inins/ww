package com.wang.social.topic.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.TagsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;

import io.reactivex.Observable;

public interface TopicContract {
    interface View extends IView {
        void showToast(String msg);
        void refreshSelectedTagLise();
    }


    interface Model extends IModel {

        Observable<BaseJson<TagsDTO>> myRecommendTag(int size, int current);
    }
}
