package com.wang.social.funshow.mvp.ui.dialog;

import android.content.Context;
import android.view.View;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;

public class MusicPopupWindow extends BasePopupWindow implements View.OnClickListener {

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
        getContentView().findViewById(R.id.pop_voice).setOnClickListener(this);
        getContentView().findViewById(R.id.pop_music).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pop_voice) {
            if (onMusicSelectListener != null) onMusicSelectListener.onVoiceClick(v);
        } else if (id == R.id.pop_music) {
            if (onMusicSelectListener != null) onMusicSelectListener.onMusicClick(v);
        }
    }

    /////////////////////////////////////////////////

    private OnMusicSelectListener onMusicSelectListener;

    public void setOnMusicSelectListener(OnMusicSelectListener onMusicSelectListener) {
        this.onMusicSelectListener = onMusicSelectListener;
    }

    public interface OnMusicSelectListener {
        void onVoiceClick(View v);

        void onMusicClick(View v);
    }
}
