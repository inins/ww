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

    private OnHandleListener mHandleListener;
    private boolean isMaster;

    public MembersAdapter(List data, OnHandleListener handleListener, boolean isMaster) {
        super(data);
        this.mHandleListener = handleListener;
        this.isMaster = isMaster;
    }

    @NonNull
    @Override
    public AbstractAdapterItem<Object> getItemView(Object type) {
        int itemType = (int) type;
        switch (itemType) {
            case ITEM_TYPE_GROUP:
                return new MemberGroupItem();
            case ITEM_TYPE_MEMBER:
                return new MemberItem(isMaster, mHandleListener);
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

    public interface OnHandleListener {

        void onFriendly(MemberInfo memberInfo);

        void onTakeOut(MemberInfo memberInfo, int position);
    }
}
