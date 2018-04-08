package com.wang.social.login.mvp.contract;

import com.frame.mvp.IModel;
import com.frame.mvp.IView;

public interface ResetPasswordContract {
    interface View extends IView {
        void showToast(String msg);
    }


    interface Model extends IModel {
    }
}
