package com.wang.social.home.applike;

import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.service.home.HomeService;
import com.wang.social.home.app.HomeServiceImpl;

/**
 * ========================================
 * 组件加载生命周期回掉
 * <p>
 * Create by ChenJing on 2018-03-23 16:40
 * ========================================
 */

public class HomeAppLike implements IApplicationLike{

    private final String HOST = "home";

    @Override
    public void onCreate() {
        UIRouter.getInstance().registerUI(HOST);
        Router.getInstance().addService(HomeService.class.getName(), new HomeServiceImpl());
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI(HOST);
        Router.getInstance().removeService(HomeService.class.getName());
    }
}
