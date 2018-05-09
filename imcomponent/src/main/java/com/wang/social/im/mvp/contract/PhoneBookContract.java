package com.wang.social.im.mvp.contract;

import com.frame.mvp.IModel;
import com.frame.mvp.IView;
import com.wang.social.im.mvp.model.entities.PhoneContact;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 15:47
 * ============================================
 */
public interface PhoneBookContract {

    interface View extends IView {

        /**
         * 显示联系人
         * @param contacts
         */
        void showContacts(List<PhoneContact> contacts);
    }

    interface Model extends IModel {

    }
}
