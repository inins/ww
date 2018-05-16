package com.frame.component.ui.acticity.PersonalCard.ui.widget;

import android.content.Context;
import android.view.View;

import com.frame.component.service.R;
import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;

public class PWFriendMoreMenu extends BasePopupWindow implements View.OnClickListener {
    public interface FriendMoreMenuCallback {
        void onSetRemark();
        void onSetAvatar();
        void onReport();
        void onDeleteFriend();
        void onAddBlackList();
    }

    private FriendMoreMenuCallback mCallback;

    public PWFriendMoreMenu(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    public void setCallback(FriendMoreMenuCallback callback) {
        mCallback = callback;
    }

    @Override
    public int getLayout() {
        return R.layout.personal_card_friend_more_menu;
    }

    @Override
    public void initBase() {
        getContentView().findViewById(R.id.more_menu_set_remark)
                .setOnClickListener(this);
        getContentView().findViewById(R.id.more_menu_set_avatar)
                .setOnClickListener(this);
        getContentView().findViewById(R.id.more_menu_report)
                .setOnClickListener(this);
        getContentView().findViewById(R.id.more_menu_delete)
                .setOnClickListener(this);
        getContentView().findViewById(R.id.more_menu_add_black_list)
                .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != mCallback) {
            if (v.getId() == R.id.more_menu_set_remark) {
                mCallback.onSetRemark();
            } else if (v.getId() == R.id.more_menu_set_avatar) {
                mCallback.onSetAvatar();
            } else if (v.getId() == R.id.more_menu_report) {
                mCallback.onReport();
            } else if (v.getId() == R.id.more_menu_delete) {
                mCallback.onDeleteFriend();
            } else if (v.getId() == R.id.more_menu_add_black_list) {
                mCallback.onAddBlackList();
            }
        }

        dismiss();
    }
}
