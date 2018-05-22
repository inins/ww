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
    // 返回选中的标签数据
    public static final int EVENTBUS_TAG_SELECTED_LIST = 0xffa105;
    // 大量知识
    public static final int EVENTBUS_TAG_ALL = 0xffa106;
    // 背景音乐选中
    public static final int EVENTBUS_BGM_SELECTED = 0xffa120;
    // 删除好友
    public static final int EVENTBUS_FRIEND_DELETE = 0xffa130;
    // 用户拉入黑名单
    public static final int EVENTBUS_FRIEND_ADD_BLACK_LIST = 0xffa131;

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
    //个人中心充值成功
    public static final int EVENT_ACCOUNT_RECHARGE_SUCCESS = 0xffa207;

    //趣晒控制器通知点赞数量，评论数量，分享数量
    public static final int EVENT_CTRL_FUNSHOW_DETAIL_DATA = 0xffa301;
    //趣晒控制器通知刷新评论
    public static final int EVENT_FUNSHOW_DETAIL_ADD_EVA = 0xffa302;
    //趣晒控制器通知刷新分享数量
    public static final int EVENT_FUNSHOW_DETAIL_ADD_SHARE = 0xffa319;
    //趣晒详情点赞通知列表更新数量
    public static final int EVENT_FUNSHOW_UPDATE_ZAN = 0xffa303;
    //趣晒详情评论点击通知编辑框
    public static final int EVENT_CTRL_FUNSHOW_DETAIL_EVAID = 0xffa304;
    //发布趣晒选择@好友
    public static final int EVENT_CTRL_FUNSHOW_ADD_USER = 0xffa305;
    //发布趣晒选择趣晒锁
    public static final int EVENT_CTRL_FUNSHOW_ADD_LOCK = 0xffa306;
    //通知趣晒被不喜欢了
    public static final int EVENT_FUNSHOW_DISSLIKE = 0xffa307;
    //趣晒列表改变了筛选类型（趣聊、老友）
    public static final int EVENT_FUNSHOW_LIST_TYPE_CHANGE = 0xffa308;

    //选择地址
    public static final int EVENT_LOCATION_SELECT = 0xffa401;

    //主工程搜索参数
    public static final int EVENT_APP_SEARCH = 0xffa501;
    // IM搜索
    public static final int EVENT_IM_SEARCH = 0xffa502;

    //有人加入游戏
    public static final int EVENT_GAME_JOIN = 0xffa601;
    //游戏结束
    public static final int EVENT_GAME_RESULT = 0xffa602;
    //游戏房间输入框高度变化
    public static final int EVENT_GAME_INPUT_HEIGHT_CHANGED = 0xffa603;

    //首页卡牌选择性别筛选条件
    public static final int EVENT_HOME_CARD_GENDER_SELECT = 0xffa701;
    //首页卡牌选择年龄筛选条件
    public static final int EVENT_HOME_CARD_AGE_SELECT = 0xffa702;

    //通知好友申请详情，处理好友申请
    public static final int EVENT_NOTIFY_DETAIL_DEAL = 0xffa801;

    //创建了一个觅聊
    public static final int EVENT_NOTIFY_CREATE_TEAM = 0xffa901;

    @IntDef({
            EVENTBUS_TAG_SELECTED,
            EVENTBUS_TAG_UNSELECT,
            EVENTBUS_TAG_DELETE,
            EVENTBUS_TAG_UPDATED,
            EVENTBUS_TAG_SELECTED_LIST,
            EVENTBUS_TAG_ALL,
            EVENTBUS_BGM_SELECTED,
            EVENTBUS_FRIEND_DELETE,
            EVENTBUS_FRIEND_ADD_BLACK_LIST,
            EVENT_MEPHOTO_CHANGE,
            EVENT_USERINFO_CHANGE,
            EVENT_LOGOUT,
            EVENT_PRIVATE_UPDATE,
            EVENT_TAB_USER,
            EVENT_ACCOUNT_EXCHANGE_STONE,
            EVENT_ACCOUNT_RECHARGE_SUCCESS,
            EVENT_CTRL_FUNSHOW_DETAIL_DATA,
            EVENT_FUNSHOW_DETAIL_ADD_EVA,
            EVENT_FUNSHOW_DETAIL_ADD_SHARE,
            EVENT_FUNSHOW_UPDATE_ZAN,
            EVENT_CTRL_FUNSHOW_DETAIL_EVAID,
            EVENT_CTRL_FUNSHOW_ADD_USER,
            EVENT_CTRL_FUNSHOW_ADD_LOCK,
            EVENT_FUNSHOW_DISSLIKE,
            EVENT_FUNSHOW_LIST_TYPE_CHANGE,
            EVENT_LOCATION_SELECT,
            EVENT_APP_SEARCH,
            EVENT_IM_SEARCH,
            EVENT_GAME_JOIN,
            EVENT_GAME_RESULT,
            EVENT_HOME_CARD_GENDER_SELECT,
            EVENT_HOME_CARD_AGE_SELECT,
            EVENT_NOTIFY_DETAIL_DEAL,
            EVENT_GAME_INPUT_HEIGHT_CHANGED,
            EVENT_NOTIFY_CREATE_TEAM
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
