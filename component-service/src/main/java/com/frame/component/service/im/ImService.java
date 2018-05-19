package com.frame.component.service.im;

import android.support.v4.app.Fragment;

import com.frame.base.BaseFragment;

/**
 * =========================================
 * <p>
 * =========================================
 */

public interface ImService {

    /**
     * 往来界面
     * @return
     */
    Fragment getContactsFragment();

    /**
     * 摇钱树聊天会话
     */
    BaseFragment getGameConversationFragment(String roomId);
}