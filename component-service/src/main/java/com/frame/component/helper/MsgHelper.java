package com.frame.component.helper;

import com.frame.component.entities.DynamicMessage;
import com.frame.component.entities.SystemMessage;
import com.frame.component.entities.msg.NotifySave;

import timber.log.Timber;

public class MsgHelper {
    public static final int CATE_NONE = -1;
    public static final int CATE_SYS = 1;
    public static final int CATE_FRIEND = 2;
    public static final int CATE_GROUP = 3;
    public static final int CATE_JOIN = 4;
    public static final int CATE_ZAN = 5;
    public static final int CATE_EVA = 6;
    public static final int CATE_AITE = 7;

    public static int getSysMsgCate(SystemMessage sysMsg) {
        switch (sysMsg.getType()) {
            case SystemMessage.TYPE_ADD_FRIEND:
                return CATE_FRIEND;
            case SystemMessage.TYPE_GROUP_APPLY:
                return CATE_GROUP;
            case SystemMessage.TYPE_GROUP_INVITE:
                return CATE_JOIN;
            default:
                return CATE_NONE;
        }
    }

    public static int getDynMsgCate(DynamicMessage dynMsg) {
        switch (dynMsg.getModeType()) {
            case DynamicMessage.TYPE_PRAISE_TOPIC:
            case DynamicMessage.TYPE_PRAISE_TOPIC_COMMENT:
            case DynamicMessage.TYPE_PRAISE_FUN_SHOW:
            case DynamicMessage.TYPE_PRAISE_FUN_SHOW_COMMENT:
                return CATE_ZAN;
            case DynamicMessage.TYPE_COMMENT_TOPIC:
            case DynamicMessage.TYPE_REPLY_TOPIC_COMMENT:
            case DynamicMessage.TYPE_COMMENT_FUN_SHOW:
            case DynamicMessage.TYPE_REPLY_FUN_SHOW_COMMENT:
                return CATE_EVA;
            case DynamicMessage.TYPE_REPLY_FUN_SHOW_AITE:
                return CATE_AITE;
            default:
                return CATE_NONE;
        }
    }


    /**
     * 持久化系统消息数量
     *
     * @param cate         消息分类
     * @param saveOrRemove true:存储 false 异常
     */
    public static void modifyNotify(int cate, boolean saveOrRemove) {
        NotifySave notifySave = AppDataHelper.getNotifySave();
        switch (cate) {
            case CATE_SYS:
                notifySave.setSysMsgCount(saveOrRemove ? 1 : 0);
                break;
            case CATE_FRIEND:
                notifySave.setFriendCount(saveOrRemove ? 1 : 0);
                break;
            case CATE_GROUP:
                notifySave.setGroupCount(saveOrRemove ? 1 : 0);
                break;
            case CATE_JOIN:
                notifySave.setJoinCount(saveOrRemove ? 1 : 0);
                break;
            case CATE_ZAN:
                notifySave.setZanCount(saveOrRemove ? 1 : 0);
                break;
            case CATE_EVA:
                notifySave.setEvaCount(saveOrRemove ? 1 : 0);
                break;
            case CATE_AITE:
                notifySave.setAiteCount(saveOrRemove ? 1 : 0);
                break;
        }
        AppDataHelper.saveNotifySave(notifySave);
        Timber.e(notifySave.toString());
    }

    /**
     * 检查某分类下是否有未读消息
     *
     * @param cate 消息分类
     */
    public static boolean hasNotify(int cate) {
        NotifySave notifySave = AppDataHelper.getNotifySave();
        switch (cate) {
            case CATE_SYS:
                return notifySave.getSysMsgCount() != 0;
            case CATE_FRIEND:
                return notifySave.getFriendCount() != 0;
            case CATE_GROUP:
                return notifySave.getGroupCount() != 0;
            case CATE_JOIN:
                return notifySave.getJoinCount() != 0;
            case CATE_ZAN:
                return notifySave.getZanCount() != 0;
            case CATE_EVA:
                return notifySave.getEvaCount() != 0;
            case CATE_AITE:
                return notifySave.getAiteCount() != 0;
            default:
                return false;
        }
    }

    /**
     * 检查所有分类，是否所有消息都已读(用于判断是否在首页和聊天页显示小红点)
     */
    public static boolean hasReadAllNotify() {
        NotifySave notifySave = AppDataHelper.getNotifySave();
        return notifySave.getSysMsgCount() == 0 &&
                notifySave.getFriendCount() == 0 &&
                notifySave.getGroupCount() == 0 &&
                notifySave.getJoinCount() == 0 &&
                notifySave.getZanCount() == 0 &&
                notifySave.getEvaCount() == 0 &&
                notifySave.getAiteCount() == 0;
    }
}
