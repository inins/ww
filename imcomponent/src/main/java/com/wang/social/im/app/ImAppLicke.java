package com.wang.social.im.app;

import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.path.ImPath;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.im.ImService;

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
        Router.getInstance().addService(ImService.class.getName(), new IMServiceImpl());
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI(ImPath.HOST);
    }
}
