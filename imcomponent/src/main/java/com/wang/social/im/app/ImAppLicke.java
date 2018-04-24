package com.wang.social.im.app;

import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.path.ImPath;
import com.frame.component.router.ui.UIRouter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-23 11:02
 * ============================================
 */
public class ImAppLicke implements IApplicationLike {

    @Override
    public void onCreate() {
        UIRouter.getInstance().registerUI(ImPath.HOST);
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI(ImPath.HOST);
    }
}
