package com.wang.social.personal.app;

import android.support.v4.app.Fragment;

import com.wang.social.personal.mvp.ui.fragment.PersonalFragment;
import com.frame.component.service.personal.PersonalService;

/**
 * ========================================
 * 向其他组件提供资源
 * <p>
 * Create by ChenJing on 2018-03-23 16:42
 * ========================================
 */

public class PersonalServiceImpl implements PersonalService {

    @Override
    public Fragment getPersonalFragment() {
        return PersonalFragment.newInstance();
    }
}
