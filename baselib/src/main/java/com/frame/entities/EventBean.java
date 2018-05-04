package com.frame.entities;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liaoinstan
 * EventBus 传递的消息对象
 */
public class EventBean implements Serializable {

    // 标签选中
    public static final int EVENTBUS_TAG_SELECTED = 0xffa101;
    // 标签取消选中
    public static final int EVENTBUS_TAG_UNSELECT = 0xffa102;
    // 标签删除
    public static final int EVENTBUS_TAG_DELETE = 0xffa103;
    // 标签编辑成功
    public static final int EVENTBUS_TAG_UPDATED = 0xffa104;
    // 返回选中的标签数据( "ids" "names")
    public static final int EVENTBUS_TAG_SELECTED_LIST = 0xffa105;
    // 背景音乐选中
    public static final int EVENTBUS_BGM_SELECTED = 0xffa106;

    //相册数量已修改，通知个人详情页面重新刷新数量
    public static final int EVENT_MEPHOTO_CHANGE = 0xffa201;
    //个人信息已修改，通知相关页面刷新
    public static final int EVENT_USERINFO_CHANGE = 0xffa202;
    //用户登出
    public static final int EVENT_LOGOUT = 0xffa203;
    //用户修改个人隐私
    public static final int EVENT_PRIVATE_UPDATE = 0xffa204;
    //用户进入个人中心tab分页
    public static final int EVENT_TAB_USER = 0xffa205;
    //个人中心兑换宝石
    public static final int EVENT_ACCOUNT_EXCHANGE_STONE = 0xffa206;

    //趣晒控制器通知点赞数量，评论数量，分享数量
    public static final int EVENT_CTRL_FUNSHOW_DETAIL_DATA = 0xffa301;
    //趣晒控制器通知刷新评论
    public static final int EVENT_FUNSHOW_DETAIL_ADD_EVA = 0xffa302;
    //趣晒详情点赞通知列表更新数量
    public static final int EVENT_FUNSHOW_UPDATE_ZAN = 0xffa303;
    //趣晒详情评论点击通知编辑框
    public static final int EVENT_CTRL_FUNSHOW_DETAIL_EVAID = 0xffa304;
    //发布趣晒选择@好友
    public static final int EVENT_CTRL_FUNSHOW_ADD_USER = 0xffa305;
    //发布趣晒选择趣晒锁
    public static final int EVENT_CTRL_FUNSHOW_ADD_LOCK = 0xffa306;
    //通知刷新趣晒列表
    public static final int EVENT_FUNSHOW_LIST_FRESH = 0xffa307;
    //趣晒列表改变了筛选类型（趣聊、老友）
    public static final int EVENT_FUNSHOW_LIST_TYPE_CHANGE = 0xffa308;

    //选择地址
    public static final int EVENT_LOCATION_SELECT = 0xffa401;

    @IntDef({
            EVENTBUS_TAG_SELECTED,
            EVENTBUS_TAG_UNSELECT,
            EVENTBUS_TAG_DELETE,
            EVENTBUS_TAG_UPDATED,
            EVENTBUS_TAG_SELECTED_LIST,
            EVENTBUS_BGM_SELECTED,
            EVENT_MEPHOTO_CHANGE,
            EVENT_USERINFO_CHANGE,
            EVENT_LOGOUT,
            EVENT_PRIVATE_UPDATE,
            EVENT_TAB_USER,
            EVENT_ACCOUNT_EXCHANGE_STONE,
            EVENT_CTRL_FUNSHOW_DETAIL_DATA,
            EVENT_FUNSHOW_DETAIL_ADD_EVA,
            EVENT_FUNSHOW_UPDATE_ZAN,
            EVENT_CTRL_FUNSHOW_DETAIL_EVAID,
            EVENT_CTRL_FUNSHOW_ADD_USER,
            EVENT_CTRL_FUNSHOW_ADD_LOCK,
            EVENT_FUNSHOW_LIST_FRESH,
            EVENT_FUNSHOW_LIST_TYPE_CHANGE,
            EVENT_LOCATION_SELECT,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventBeanType {
    }

    private @EventBeanType
    int event;
    private Map<String, Object> map = new HashMap<>();

    public EventBean() {
    }

    public EventBean(@EventBeanType int event) {
        this.event = event;
    }

    public EventBean put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Object get(String key) {
        return map.get(key);
    }

    //////////get & set///////////////

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
