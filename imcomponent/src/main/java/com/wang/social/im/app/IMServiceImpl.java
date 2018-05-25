package com.wang.social.im.app;

import android.app.Application;
import android.support.v4.app.Fragment;

import com.frame.base.BaseFragment;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.service.im.ImService;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.helper.FriendShipHelper;
import com.wang.social.im.helper.GroupHelper;
import com.wang.social.im.helper.ImHelper;
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

    @Override
    public void imLogout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    @Override
    public void imLogin(Application application) {
        TIMManager.getInstance().login(String.valueOf(AppDataHelper.getUser().getUserId()), AppDataHelper.getSign(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                ImHelper.configurationOfflinePush(application);

                GroupHelper.getInstance();
                FriendShipHelper.getInstance();
            }
        });
    }
}
