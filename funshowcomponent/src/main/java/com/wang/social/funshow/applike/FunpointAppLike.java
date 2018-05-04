package com.wang.social.funshow.applike;

import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.service.personal.PersonalService;
import com.wang.social.funshow.app.FunshowServiceImpl;

/**
 * ========================================
 * 组件加载生命周期回掉
 * <p>
 * Create by ChenJing on 2018-03-23 16:40
 * ========================================
 */

public class FunpointAppLike implements IApplicationLike{

    private final String HOST = "funshow";

    @Override
    public void onCreate() {
        UIRouter.getInstance().registerUI(HOST);
        Router.getInstance().addService(FunshowService.class.getName(), new FunshowServiceImpl());
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI(HOST);
        Router.getInstance().removeService(FunpointService.class.getName());
    }
}
