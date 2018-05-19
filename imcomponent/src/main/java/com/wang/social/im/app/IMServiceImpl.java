package com.wang.social.im.app;

import android.support.v4.app.Fragment;

import com.frame.base.BaseFragment;
import com.frame.component.service.im.ImService;
import com.wang.social.im.mvp.ui.fragments.ContactsFragment;
import com.wang.social.im.mvp.ui.fragments.GameConversationFragment;

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

    @Override
    public BaseFragment getGameConversationFragment(String roomId) {
        return GameConversationFragment.newInstance(roomId);
    }
}
