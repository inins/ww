package com.wang.social.funshow.mvp.contract;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.user.TopUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:06
 * =========================================
 */

public interface FunshowListContract {

    interface View extends IView {
        void reFreshList(List<FunshowBean> datas);

        void appendList(List<FunshowBean> datas);

        void finishSpringView();

        void reFreshTopUsers(List<TopUser> topUsers);

        void callRefresh();
    }

    interface Model extends IModel {
        Observable<BaseJson<BaseListWrap<Funshow>>> getFunshowList(int type, int current, int size);

        Observable<BaseJson<BaseListWrap<TopUser>>> getFunshowTopUserList(String from, int current, int size);

        Observable<BaseJson<Object>> shatDownUser(String shieldUserId, int type);
    }
}
