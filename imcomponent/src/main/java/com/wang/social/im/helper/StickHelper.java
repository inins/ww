package com.wang.social.im.helper;

import com.frame.utils.SPUtils;
import com.wang.social.im.mvp.model.entities.Stick;
import com.wang.social.im.mvp.model.entities.UIConversation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-12 11:45
 * ============================================
 */
public class StickHelper {

    private static final String SP_NAME = "Stick";
    private static final String SP_KEY = "sticks";

    private volatile static StickHelper mInstance;

    private List<Stick> mSticks;

    private StickHelper() {
        mSticks = new ArrayList<>();

        refresh();
    }

    public static StickHelper getInstance() {
        if (mInstance == null) {
            synchronized (StickHelper.class) {
                if (mInstance == null) {
                    mInstance = new StickHelper();
                }
            }
        }
        return mInstance;
    }

    private void refresh() {
        List<Stick> sticks = (List<Stick>) SPUtils.getInstance(SP_NAME).get(SP_KEY);
        if (sticks != null && sticks.size() > 0) {
            mSticks.addAll(sticks);
        }
    }

    /**
     * 判断会话是否置顶
     *
     * @param uiConversation
     * @return
     */
    public boolean isStick(UIConversation uiConversation) {
        for (Stick stick : mSticks) {
            if (stick.getCvTypeOrdinal() == uiConversation.getConversationType().ordinal() &&
                    stick.getIdentity().equals(uiConversation.getIdentify())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 置顶/取消置顶
     *
     * @param uiConversation
     */
    public void toggleStick(UIConversation uiConversation) {
        Iterator<Stick> iterator = mSticks.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            Stick item = iterator.next();
            if (item.getCvTypeOrdinal() == uiConversation.getConversationType().ordinal() &&
                    item.getIdentity().equals(uiConversation.getIdentify())) {
                iterator.remove();
                removed = true;
                break;
            }
        }
        if (!removed) {
            Stick stick = new Stick();
            stick.setIdentity(uiConversation.getIdentify());
            stick.setCvTypeOrdinal(uiConversation.getConversationType().ordinal());
            mSticks.add(stick);
        }
        SPUtils.getInstance(SP_NAME).put(SP_KEY, mSticks);
    }

    /**
     * 移除
     *
     * @param uiConversation
     */
    public void removeIfExit(UIConversation uiConversation) {
        Iterator<Stick> iterator = mSticks.iterator();
        while (iterator.hasNext()) {
            Stick item = iterator.next();
            if (item.getCvTypeOrdinal() == uiConversation.getConversationType().ordinal() &&
                    item.getIdentity().equals(uiConversation.getIdentify())) {
                iterator.remove();
                SPUtils.getInstance(SP_NAME).put(SP_KEY, mSticks);
                break;
            }
        }
    }
}
