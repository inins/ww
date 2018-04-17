package com.wang.social.im.helper;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendGroup;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxy;
import com.wang.social.im.mvp.model.entities.FriendProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================
 * 好友信息
 * <p>
 * Create by ChenJing on 2018-04-17 13:35
 * ============================================
 */
public class FriendShipHelper {

    //好友分组
    private List<String> mGroups;
    private Map<String, List<FriendProfile>> mFriends;

    private volatile static FriendShipHelper mInstance;

    private FriendShipHelper() {
        mGroups = new ArrayList<>();
        mFriends = new HashMap<>();

        refresh();
    }

    public static FriendShipHelper getInstance() {
        if (mInstance == null) {
            synchronized (FriendShipHelper.class) {
                if (mInstance == null) {
                    mInstance = new FriendShipHelper();
                }
            }
        }
        return mInstance;
    }

    public void refresh() {
        mGroups.clear();
        mFriends.clear();
        List<TIMFriendGroup> friendGroups = TIMFriendshipProxy.getInstance().getFriendsByGroups(null);
        if (friendGroups == null) return;
        for (TIMFriendGroup group : friendGroups) {
            mGroups.add(group.getGroupName());
            List<FriendProfile> profiles = new ArrayList<>();
            for (TIMUserProfile userProfile : group.getProfiles()) {
                profiles.add(new FriendProfile(userProfile));
            }
            mFriends.put(group.getGroupName(), profiles);
        }
    }

    /**
     * 根据好友ID查询好友信息
     * @param identify
     * @return
     */
    public FriendProfile getFriendProfile(String identify) {
        for (String key : mFriends.keySet()) {
            for (FriendProfile profile : mFriends.get(key)) {
                if (profile.getIdentify().equals(identify)) {
                    return profile;
                }
            }
        }
        return null;
    }
}
