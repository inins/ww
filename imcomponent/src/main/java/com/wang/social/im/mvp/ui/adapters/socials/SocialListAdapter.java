package com.wang.social.im.mvp.ui.adapters.socials;

import android.support.annotation.NonNull;

import com.wang.social.im.mvp.model.entities.SocialListLevelOne;
import com.wang.social.im.mvp.model.entities.SocialListLevelThree;
import com.wang.social.im.mvp.model.entities.SocialListLevelTwo;
import com.wang.social.im.view.expand.adapter.BaseExpandableAdapter;
import com.wang.social.im.view.expand.viewholder.AbstractAdapterItem;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 10:21
 * ============================================
 */
public class SocialListAdapter extends BaseExpandableAdapter {

    /*
        我创建的/加入的
     */
    private final int ITEM_TYPE_CATEGORY = 1;
    /*
        趣聊
     */
    private final int ITEM_TYPE_SOCIAL = 2;
    /*
        觅聊
     */
    private final int ITEM_TYPE_TEAMS = 3;

    public SocialListAdapter(List data) {
        super(data);
    }

    @NonNull
    @Override
    public AbstractAdapterItem<Object> getItemView(Object type) {
        int itemType = (int) type;
        switch (itemType) {
            case ITEM_TYPE_CATEGORY:
                return new LevelOneItem();
            case ITEM_TYPE_SOCIAL:
                return new LevelTwoItem();
            case ITEM_TYPE_TEAMS:
                return new TeamsItem();
        }
        return null;
    }

    @Override
    public Object getItemViewType(Object t) {
        if (t instanceof SocialListLevelOne) {
            return ITEM_TYPE_CATEGORY;
        } else if (t instanceof SocialListLevelTwo) {
            return ITEM_TYPE_SOCIAL;
        } else if (t instanceof SocialListLevelThree) {
            return ITEM_TYPE_TEAMS;
        }
        return super.getItemViewType(t);
    }
}
