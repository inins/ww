package com.wang.social.funpoint.app;

import android.support.v4.app.Fragment;

import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.personal.PersonalService;
import com.wang.social.funpoint.mvp.ui.fragment.FunPointFragment;

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
}
