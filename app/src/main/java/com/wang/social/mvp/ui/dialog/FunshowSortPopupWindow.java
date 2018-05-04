package com.wang.social.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckedTextView;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.wang.social.R;

import org.greenrobot.eventbus.EventBus;

public class FunshowSortPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private CheckedTextView btn_funchat;
    private CheckedTextView btn_friend;

    public FunshowSortPopupWindow(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    @Override
    public int getLayout() {
        return R.layout.pop_funshow_sort;
    }

    @Override
    public void initBase() {
        btn_funchat = getContentView().findViewById(R.id.btn_funchat);
        btn_friend = getContentView().findViewById(R.id.btn_friend);
        btn_funchat.setOnClickListener(this);
        btn_friend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_funchat) {
            btn_funchat.setChecked(true);
            btn_friend.setChecked(false);
            EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_LIST_TYPE_CHANGE).put("type", 0));
            dismiss();
            if (onFunshowSortClickListener != null) onFunshowSortClickListener.onFunChatClick(v);
        } else if (id == R.id.btn_friend) {
            btn_funchat.setChecked(false);
            btn_friend.setChecked(true);
            EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_LIST_TYPE_CHANGE).put("type", 1));
            dismiss();
            if (onFunshowSortClickListener != null) onFunshowSortClickListener.onFriendClick(v);
        }
    }

    @Override
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            int x = parent.getWidth() - this.getWidth() + SizeUtils.dp2px(10);
            this.showAsDropDown(parent, x, 0);
            if (needanim) turnBackgroundDark();
        } else {
            this.dismiss();
        }
    }

    ////////////////////////////////////////////

    private OnFunshowSortClickListener onFunshowSortClickListener;

    public void setOnFunshowSortClickListener(OnFunshowSortClickListener onFunshowSortClickListener) {
        this.onFunshowSortClickListener = onFunshowSortClickListener;
    }

    public interface OnFunshowSortClickListener {
        void onFunChatClick(View v);

        void onFriendClick(View v);
    }
}
