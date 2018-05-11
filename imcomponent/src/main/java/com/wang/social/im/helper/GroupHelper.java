package com.wang.social.im.helper;

import com.tencent.imsdk.ext.group.TIMGroupAssistant;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.model.entities.GroupProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================
 * 群组相关通用方法
 * <p>
 * Create by ChenJing on 2018-04-17 10:58
 * ============================================
 */
public class GroupHelper {

    private volatile static GroupHelper mInstance;

    private Map<String, List<GroupProfile>> groups;

    private GroupHelper() {
        groups = new HashMap<>();
        groups.put(IMConstants.IM_GROUP_TYPE_PUBLIC, new ArrayList<>());
        groups.put(IMConstants.IM_GROUP_TYPE_PRIVATE, new ArrayList<>());
        groups.put(IMConstants.IM_GROUP_TYPE_CHAT_ROOM, new ArrayList<>());

        refresh();
    }

    public static GroupHelper getInstance() {
        if (mInstance == null) {
            synchronized (GroupHelper.class) {
                if (mInstance == null) {
                    mInstance = new GroupHelper();
                }
            }
        }
        return mInstance;
    }

    private void refresh() {
        for (String key : groups.keySet()) {
            groups.get(key).clear();
        }
        List<TIMGroupCacheInfo> groupInfos = TIMGroupAssistant.getInstance().getGroups(null);
        if (groupInfos == null) return;
        for (TIMGroupCacheInfo cacheInfo : groupInfos) {
            List<GroupProfile> list = groups.get(cacheInfo.getGroupInfo().getGroupType());
            if (list == null) continue;
            list.add(new GroupProfile(cacheInfo));
        }
    }

    /**
     * 通过群ID查询群资料
     *
     * @param identify
     * @return
     */
    public GroupProfile getGroupProfile(String identify) {
        for (String key : groups.keySet()) {
            for (GroupProfile profile : groups.get(key)) {
                if (profile.getIdentify().equals(identify)) {
                    return profile;
                }
            }
        }
        return null;
    }

    /**
     * 根据指定类型，通过群ID查询群资料
     *
     * @param type
     * @param identify
     * @return
     */
    public GroupProfile getGroupProfile(String type, String identify) {
        for (GroupProfile profile : groups.get(type)) {
            if (profile.getIdentify().equals(identify)) {
                return profile;
            }
        }
        return null;
    }
}
