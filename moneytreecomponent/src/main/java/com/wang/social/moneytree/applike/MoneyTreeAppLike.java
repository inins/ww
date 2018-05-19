package com.wang.social.moneytree.applike;

import com.frame.component.applicationlike.IApplicationLike;
import com.frame.component.router.Router;
import com.frame.component.router.ui.UIRouter;
import com.wang.social.moneytree.mvp.model.api.MoneyTreeService;

/**
 * ========================================
 * 组件加载生命周期回掉
 * <p>
 * Create by ChenJing on 2018-03-23 16:40
 * ========================================
 */

public class MoneyTreeAppLike implements IApplicationLike {

    private final String HOST = "moneytree";

    @Override
    public void onCreate() {
        UIRouter.getInstance().registerUI(HOST);
    }

    @Override
    public void onStop() {
        UIRouter.getInstance().unregisterUI(HOST);
    }
}
