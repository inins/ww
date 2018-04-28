package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;
import com.wang.social.im.mvp.model.entities.dto.EnvelopAdoptInfoDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-24 13:48
 * ============================================
 */
public interface EnvelopDetailContract {

    interface View extends IView {

        /**
         * 显示领取记录
         *
         * @param list
         * @param hasMore
         */
        void showList(List<EnvelopAdoptInfo> list, boolean hasMore);

        void onLoadListError();
    }

    interface Model extends IModel {

        /**
         * 获取红包领取记录
         *
         * @param envelopId
         * @param currentPage
         * @return
         */
        Observable<BaseJson<PageListDTO<EnvelopAdoptInfoDTO, EnvelopAdoptInfo>>> getAdoptList(long envelopId, int currentPage);
    }
}
