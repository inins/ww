package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.CreateGroupResult;
import com.wang.social.im.mvp.model.entities.SocialAttribute;
import com.wang.social.im.mvp.model.entities.dto.CreateGroupResultDTO;
import com.wang.social.im.mvp.model.entities.dto.PayCheckInfoDTO;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-02 20:24
 * ============================================
 */
public interface CreateSocialContract {

    interface View extends IView {

        /**
         * 创建完成
         * @param result
         */
        void onCreateComplete(CreateGroupResult result);

        /**
         * 显示支付弹框
         */
        void showPayDialog();
    }

    interface Model extends IModel {

        /**
         * 检查是否需要支付
         *
         * @return
         */
        Observable<BaseJson<PayCheckInfoDTO>> checkCreateSocialStatus();

        /**
         * 创建趣聊
         *
         * @param orderId       订单ID
         * @param name          名称
         * @param desc          描述
         * @param cover         封面
         * @param head          头像
         * @param attr          gender        性别限制 (不限传null,男：0，女：1)
         *                      ageLimit      年龄限制 (以","分割)
         * @param canCreateTeam 成员是否可以创建觅聊
         * @param tags          标签ID（以","分割）
         * @return
         */
        Observable<BaseJson<CreateGroupResultDTO>> createSocial(String orderId, String name, String desc, String cover, String head, SocialAttribute attr, boolean canCreateTeam, String tags);
    }
}
