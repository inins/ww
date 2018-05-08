package com.wang.social.funshow.app;

import android.support.v4.app.Fragment;

import com.frame.component.service.funshow.FunshowService;
import com.wang.social.funshow.mvp.ui.fragment.FunShowFragment;
import com.wang.social.funshow.mvp.ui.fragment.SearchFunshowFragment;

/**
 * ========================================
 * 向其他组件提供资源
 * ========================================
 */

public class FunshowServiceImpl implements FunshowService {

    @Override
    public Fragment getFunshowFragment() {
        return FunShowFragment.newInstance();
    }

    @Override
    public Fragment getSearchFunshowFragment() {
        return SearchFunshowFragment.newInstance();
    }
}
