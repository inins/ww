package com.wang.social.im.app;

import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMUserConfig;

import java.util.List;
import java.util.Observable;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-22 14:42
 * ============================================
 */
public class RefreshEvent extends Observable implements TIMRefreshListener {

    private volatile static RefreshEvent mInstance;

    private RefreshEvent() {
    }

    public static RefreshEvent getInstance() {
        if (mInstance == null) {
            synchronized (RefreshEvent.class) {
                if (mInstance == null) {
                    mInstance = new RefreshEvent();
                }
            }
        }
        return mInstance;
    }

    public void init(TIMUserConfig userConfig) {
        userConfig.setRefreshListener(this);
    }

    /**
     * 数据刷新通知,如未读计数、会话列表
     */
    @Override
    public void onRefresh() {
        setChanged();
        notifyObservers();
    }

    @Override
    public void onRefreshConversation(List<TIMConversation> list) {
        setChanged();
        notifyObservers();
    }
}
