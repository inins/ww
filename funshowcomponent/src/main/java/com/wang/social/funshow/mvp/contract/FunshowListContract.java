package com.wang.social.funshow.mvp.contract;

import com.frame.component.entities.BaseListWrap;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.user.TopUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:06
 * =========================================
 */

public interface FunshowListContract {

    interface View extends IView {
        void reFreshList(List<Funshow> datas);

        void appendList(List<Funshow> datas);

        void finishSpringView();

        void reFreshTopUsers(List<TopUser> topUsers);
    }

    interface Model extends IModel {
        Observable<BaseJson<BaseListWrap<Funshow>>> getFunshowList(int type, int current, int size);

        Observable<BaseJson<BaseListWrap<TopUser>>> getFunshowTopUserList(String from, int current, int size);
    }
}