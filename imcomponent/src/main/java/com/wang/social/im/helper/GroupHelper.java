package com.wang.social.im.helper;

import com.tencent.imsdk.ext.group.TIMGroupAssistant;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
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
    private static final String PUBLIC_GROUP = "Public", PRIVATE_GROUP = "Private", CHAT_ROOM = "ChatRoom";

    private GroupHelper() {
        groups = new HashMap<>();
        groups.put(PUBLIC_GROUP, new ArrayList<>());
        groups.put(PRIVATE_GROUP, new ArrayList<>());
        groups.put(CHAT_ROOM, new ArrayList<>());

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
     * @param identify
     * @return
     */
    public GroupProfile getGroupProfile(String identify){
        for (String key : groups.keySet()){
            for (GroupProfile profile : groups.get(key)){
                if (profile.getIdentify().equals(identify)){
                    return profile;
                }
            }
        }
        return null;
    }

    /**
     * 根据指定类型，通过群ID查询群资料
     * @param type
     * @param identify
     * @return
     */
    public GroupProfile getGroupProfile(String type, String identify){
        for (GroupProfile profile : groups.get(type)){
            if (profile.getIdentify().equals(identify)){
                return profile;
            }
        }
        return null;
    }
}
