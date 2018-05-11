package com.wang.social.im.mvp.ui.adapters.members;

import android.support.annotation.NonNull;

import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.view.expand.adapter.BaseExpandableAdapter;
import com.wang.social.im.view.expand.viewholder.AbstractAdapterItem;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:14
 * ============================================
 */
public class MembersAdapter extends BaseExpandableAdapter {

    private final int ITEM_TYPE_GROUP = 1;
    private final int ITEM_TYPE_MEMBER = 2;

    public MembersAdapter(List data) {
        super(data);
    }

    @NonNull
    @Override
    public AbstractAdapterItem<Object> getItemView(Object type) {
        int itemType = (int) type;
        switch (itemType) {
            case ITEM_TYPE_GROUP:
                return new MemberGroupItem();
            case ITEM_TYPE_MEMBER:
                return new MemberItem();
        }
        return null;
    }

    @Override
    public Object getItemViewType(Object t) {
        if (t instanceof MembersLevelOne) {
            return ITEM_TYPE_GROUP;
        } else if (t instanceof MemberInfo) {
            return ITEM_TYPE_MEMBER;
        }
        return super.getItemViewType(t);
    }
}
