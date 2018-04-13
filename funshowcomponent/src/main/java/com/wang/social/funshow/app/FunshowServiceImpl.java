package com.wang.social.funshow.app;

import android.support.v4.app.Fragment;

import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.service.personal.PersonalService;
import com.wang.social.funshow.ui.fragment.FunShowFragment;

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
}
