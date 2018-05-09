package com.wang.social.im.mvp.contract;

import com.frame.component.enums.Gender;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.ShadowInfo;
import com.wang.social.im.mvp.model.entities.dto.PayCheckInfoDTO;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 16:47
 * ============================================
 */
public interface ShadowSettingContract {

    interface View extends IView{

        /**
         * 显示支付弹框
         */
        void showPayDialog(String applyId, ShadowInfo info);

        /**
         * 修改完成
         * @param info
         */
        void onUpdateComplete(ShadowInfo info);
    }

    interface Model extends IModel{

        /**
         * 检查修改分身信息是否需要支付
         * @param groupId
         * @return
         */
        Observable<BaseJson<PayCheckInfoDTO>> checkShadowStatus(String groupId);

        /**
         * 修改分身信息
         * @param socialId
         * @param orderId
         * @param nickname
         * @param portrait
         * @return
         */
        Observable<BaseJson> updateShadowInfo(String socialId, String orderId, String nickname, String portrait, Gender gender);
    }
}
