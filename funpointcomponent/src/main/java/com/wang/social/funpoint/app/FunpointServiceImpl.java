package com.wang.social.funpoint.app;

import android.support.v4.app.Fragment;

import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.personal.PersonalService;
import com.wang.social.funpoint.mvp.ui.fragment.FunPointFragment;
import com.wang.social.funpoint.mvp.ui.fragment.SearchFunpointFragment;

/**
 * ========================================
 * 向其他组件提供资源
 * ========================================
 */

public class FunpointServiceImpl implements FunpointService {

    @Override
    public Fragment getFunpointFragment() {
        return FunPointFragment.newInstance();
    }

    @Override
    public Fragment getSearchFunpointFragment() {
        return SearchFunpointFragment.newInstance();
    }
}
