package com.wang.social.app;

import com.frame.base.BaseApplication;
import com.frame.component.router.ui.UIRouter;
import com.frame.utils.ToastUtil;
import com.umeng.commonsdk.UMConfigure;
import com.wang.social.socialize.SocializeUtil;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-23 15:27
 * ========================================
 */

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this,
                UMConfigure.DEVICE_TYPE_PHONE,
                "");

        SocializeUtil.init(this);

        UIRouter.getInstance().registerUI("app");
    }
}
