package com.wang.social.home.app;

import android.support.v4.app.Fragment;

import com.frame.component.service.home.HomeService;
import com.wang.social.home.mvp.ui.fragment.HomeFragment;

/**
 * ========================================
 * 向其他组件提供资源
 * ========================================
 */

public class HomeServiceImpl implements HomeService {

    @Override
    public Fragment getHomeFragment() {
        return HomeFragment.newInstance();
    }
}
