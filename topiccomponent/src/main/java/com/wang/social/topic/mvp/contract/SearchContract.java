package com.wang.social.topic.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.SearchResultsDTO;
import com.wang.social.topic.mvp.model.entities.dto.TopicRspDTO;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;

public interface SearchContract {
    interface View extends IView {
        void showToast(String msg);
        void onSearchSuccess();
        void onSearchCompleted();
    }


    interface Model extends IModel {
        Observable<BaseJson<SearchResultsDTO>> searchTopic(String keyword, String tagNames, int size, int current);
    }
}
