package com.wang.social.funshow.mvp.ui.dialog;

import android.content.Context;
import android.view.View;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;

public class MusicPopupWindow extends BasePopupWindow {

    public MusicPopupWindow(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    @Override
    public int getLayout() {
        return R.layout.funshow_pop_music;
    }

    @Override
    public void initBase() {

    }

    @Override
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popHight = this.getContentView().getMeasuredHeight();
            int parentHight = parent.getHeight();
            int x = -(this.getWidth() - parent.getWidth());
            int y = -(popHight + parentHight - SizeUtils.dp2px(5));
            this.showAsDropDown(parent, x, y);
            if (needanim) turnBackgroundDark();
        } else {
            this.dismiss();
        }
    }
}
