package com.wang.social.personal.applike;

import com.wang.social.personal.app.PersonalServiceImpl;
import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.frame.component.service.personal.PersonalService;

/**
 * ========================================
 * 组件加载生命周期回掉
 * <p>
 * Create by ChenJing on 2018-03-23 16:40
 * ========================================
 */

public class PersonalAppLike implements IApplicationLike{

    private final String HOST = "personal";

    @Override
    public void onCreate() {
        UIRouter.getInstance().registerUI(HOST);
        Router.getInstance().addService(PersonalService.class.getName(), new PersonalServiceImpl());
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI(HOST);
        Router.getInstance().removeService(PersonalService.class.getName());
    }
}
