package com.wang.social.login.mvp.contract;

import com.frame.mvp.IModel;
import com.frame.mvp.IView;

public interface TagListContract {
    interface View extends IView {
        void tagListChanged();
    }


    interface Model extends IModel {
    }
}
