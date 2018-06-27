package com.wang.social.login202.mvp.applike;

import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.router.ui.UIRouter;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-23 14:58
 * ========================================
 */

public class Login202AppLike implements IApplicationLike{

    @Override
    public void onCreate() {
        UIRouter.getInstance().registerUI("login202");
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI("login202");
    }
}
