package com.wang.social.im.mvp.contract;

import android.app.Activity;

import com.frame.http.api.BaseJson;
import com.frame.http.api.PageListDTO;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.OfficialImage;
import com.wang.social.im.mvp.model.entities.dto.OfficialImageDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-24 9:16
 * ============================================
 */
public interface BackgroundSettingContract {

    interface View extends IView {

        void showImages(List<OfficialImage> images, boolean hasMore);

        void hideListLoading();

        Activity getActivity();
    }

    interface Model extends IModel {

        /**
         * 获取官方图片列表
         *
         * @return
         */
        Observable<BaseJson<PageListDTO<OfficialImageDTO, OfficialImage>>> getOfficialImages(int currentPage);
    }
}