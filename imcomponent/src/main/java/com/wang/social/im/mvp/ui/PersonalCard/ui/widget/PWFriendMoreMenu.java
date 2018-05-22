package com.wang.social.im.mvp.ui.PersonalCard.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;

public class PWFriendMoreMenu extends BasePopupWindow implements View.OnClickListener {
    public interface FriendMoreMenuCallback {
        void onSetRemark();
        void onSetAvatar();
        void onReport();
        void onDeleteFriend();
        void onAddBlackList();
    }

    // 点击回调
    private FriendMoreMenuCallback mCallback;
    // 是否黑名单
    private int mIsBlack;
    private TextView mBlackListTV;

    public PWFriendMoreMenu(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    public void setCallback(FriendMoreMenuCallback callback) {
        mCallback = callback;
    }

    public void setBlack(int black) {
        mIsBlack = black;

        if (null == mBlackListTV) return;

        // 0：未拉黑，大于0：黑名单
        if (mIsBlack > 0) {
            mBlackListTV.setText(getContentView()
                    .getResources()
                    .getString(R.string.im_personal_card_more_menu_delete_black_list));
        } else {
            mBlackListTV.setText(getContentView()
                    .getResources()
                    .getString(R.string.im_personal_card_more_menu_add_black_list));
        }
    }

    @Override
    public int getLayout() {
        return R.layout.im_personal_card_friend_more_menu;
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

        mBlackListTV = getContentView().findViewById(R.id.more_menu_add_black_list);
        // 0：未拉黑，大于0：黑名单
        if (mIsBlack > 0) {
            mBlackListTV.setText(getContentView()
                    .getResources()
                    .getString(R.string.im_personal_card_more_menu_delete_black_list));
        } else {
            mBlackListTV.setText(getContentView()
                    .getResources()
                    .getString(R.string.im_personal_card_more_menu_add_black_list));
        }
        mBlackListTV.setOnClickListener(this);
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
