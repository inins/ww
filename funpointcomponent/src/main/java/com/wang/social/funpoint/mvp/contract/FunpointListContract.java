package com.wang.social.funpoint.mvp.contract;

import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.funpoint.mvp.entities.Funpoint;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

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

        void finishSpringView();
    }

    interface Model extends IModel {

        Observable<BaseJson<BaseListWrap<Funpoint>>> getFunpointList(int isCondition, int current, int size);
    }
}
