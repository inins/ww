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
    //相册数量已修改，通知个人详情页面重新刷新数量
    public static final int EVENT_MEPHOTO_CHANGE = 0xffa201;
    //个人信息已修改，通知相关页面刷新
    public static final int EVENT_USERINFO_CHANGE = 0xffa202;

    @IntDef({
            EVENTBUS_TAG_SELECTED,
            EVENTBUS_TAG_UNSELECT,
            EVENTBUS_TAG_DELETE,
            EVENT_MEPHOTO_CHANGE,
            EVENT_USERINFO_CHANGE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventBeanType {
    }

    private @EventBeanType int event;
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
