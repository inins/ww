package com.wang.social.topic.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.topic.mvp.model.entities.dto.TemplatesDTO;

import io.reactivex.Observable;

public interface TemplateContract {
    interface View extends IView {
        String getDefaultName();
        void onTemplateListLoaded();
        void onNotifyTemplatesChanged();
    }


    interface Model extends IModel {
        Observable<BaseJson<TemplatesDTO>> templeList(int type, int size, int current);
    }
}
