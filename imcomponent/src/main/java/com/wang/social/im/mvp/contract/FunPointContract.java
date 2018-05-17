package com.wang.social.im.mvp.contract;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-17 10:11
 * ============================================
 */
public interface FunPointContract {

    interface View extends IView {

        /**
         * 显示趣点
         *
         * @param data
         * @param hasMore
         */
        void showFunPoints(List<Funpoint> data, boolean hasMore);
    }

    interface Model extends IModel {

        /**
         * 获取趣点列表
         *
         * @return
         */
        Observable<BaseJson<BaseListWrap<Funpoint>>> getFunPointList(String teamId, int currentPage, int pageSize);
    }
}
