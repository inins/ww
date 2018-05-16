package com.wang.social.im.app;

import android.support.v4.app.Fragment;

import com.frame.component.service.im.ImService;
import com.wang.social.im.mvp.ui.fragments.ContactsFragment;

/**
 * ========================================
 * 向其他组件提供资源
 * ========================================
 */

public class IMServiceImpl implements ImService {
    @Override
    public Fragment getContactsFragment() {
        return ContactsFragment.newInstance();
    }
}
