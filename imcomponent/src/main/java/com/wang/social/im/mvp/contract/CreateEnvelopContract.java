package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.dto.CreateEnvelopResultDTO;

import io.reactivex.Observable;

/**
 * ============================================
 * 发送红包
 * <p>
 * Create by ChenJing on 2018-04-24 10:02
 * ============================================
 */
public interface CreateEnvelopContract {

    interface View extends IView {

        /**
         * 创建成功
         *
         * @param envelopId
         */
        void onCreateSuccess(long envelopId, String message);
    }

    interface Model extends IModel {

        /**
         * 创建红包
         *
         * @param envelopType
         * @param diamond
         * @param count
         * @param message
         * @param groupId
         * @return
         */
        Observable<BaseJson<CreateEnvelopResultDTO>> createEnvelop(EnvelopInfo.EnvelopType envelopType, int diamond, int count, String message, String groupId);
    }
}
