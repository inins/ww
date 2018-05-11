package com.wang.social.home.mvp.contract;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.Tag;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.http.api.BaseJson;
import com.frame.mvp.IModel;
import com.frame.mvp.IView;

import java.util.List;

import io.reactivex.Observable;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 14:06
 * =========================================
 */

public interface HomeContract {

    interface View extends IView {
        void finishSpringView();
    }
}
