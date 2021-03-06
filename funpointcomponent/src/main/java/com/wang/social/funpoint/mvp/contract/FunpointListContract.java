package com.wang.social.funpoint.mvp.contract;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Tag;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.frame.component.entities.funpoint.Funpoint;

import java.util.List;

import io.reactivex.Observable;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:06
 * =========================================
 */

public interface FunpointListContract {

    interface View extends IView {
        void reFreshList(List<Funpoint> datas);

        void appendList(List<Funpoint> datas);

        void reFreshReadCountById(int newsId);

        void reFreshTags(List<Tag> tags);

        void finishSpringView();
    }

    interface Model extends IModel {

        Observable<BaseJson<BaseListWrap<Funpoint>>> getFunpointList(int isCondition, int current, int size);

//        Observable<BaseJson<Object>> readFunpoint(int newsId);

        Observable<BaseJson<BaseListWrap<Tag>>> getRecommendTag();
    }
}
