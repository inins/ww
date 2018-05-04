package com.wang.social.topic.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;

import java.util.Map;

import io.reactivex.Observable;

public interface ReleaseTopicContract {
    interface View extends IView {
        void toastLong(String msg);
        void onAddTopicSuccess();
    }


    interface Model extends IModel {
        Observable<BaseJson> addTopic(String title, String content, String firstStrff,
                                      String tagIds, int relateState, int gemstone,
                                      String longitude, String latitude, String adcode,
                                      String address, int templateId, String backgroundImage,
                                      int backgroundMusicId, String informUserIds);
        Observable<BaseJson> addTopic(Map<String, Object> param);
    }
}
