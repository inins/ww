package com.wang.social.im.mvp.contract;

import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;
import com.wang.social.im.mvp.model.entities.SimpleSocial;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.SimpleGroupInfoDTO;
import com.wang.social.im.mvp.model.entities.dto.SimpleSocialDTO;

import java.util.List;

import io.reactivex.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-06-14 10:10
 * ============================================
 */
public interface CTSocialListContract {

    interface View extends IView {

        /**
         * 显示趣聊列表
         *
         * @param socials
         */
        void showSocials(List<SimpleSocial> socials);
    }

    interface Model extends IModel {

        /**
         * 获取我的趣聊
         *
         * @return
         */
        Observable<BaseJson<ListDataDTO<SimpleSocialDTO, SimpleSocial>>> getSocials();
    }
}
