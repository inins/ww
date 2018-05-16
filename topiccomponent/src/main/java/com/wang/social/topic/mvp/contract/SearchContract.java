package com.wang.social.topic.mvp.contract;

import com.frame.component.entities.Topic;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.SearchResultsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicDTO;

import io.reactivex.Observable;

public interface SearchContract {
    interface View extends IView {
        void showToast(String msg);
        void onSearchSuccess();
        void onSearchCompleted();
    }


    interface Model extends IModel {
        Observable<BaseJson<PageListDTO<TopicDTO, Topic>>> searchTopic(String keyword, String tagNames, int size, int current);
    }
}
