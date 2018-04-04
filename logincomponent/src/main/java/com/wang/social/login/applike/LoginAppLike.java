package com.wang.social.login.applike;

import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.router.ui.UIRouter;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-23 14:58
 * ========================================
 */

public class LoginAppLike implements IApplicationLike{

    @Override
    public void onCreate() {
        UIRouter.getInstance().registerUI("passwordLogin");
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI("passwordLogin");
    }
}
