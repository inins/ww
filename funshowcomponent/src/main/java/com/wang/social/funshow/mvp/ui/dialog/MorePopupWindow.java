package com.wang.social.funshow.mvp.ui.dialog;

import android.content.Context;
import android.view.View;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.wang.social.funshow.R;

public class MorePopupWindow extends BasePopupWindow implements View.OnClickListener {

    public MorePopupWindow(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    @Override
    public int getLayout() {
        return R.layout.funshow_pop_dislike;
    }

    @Override
    public void initBase() {
        getContentView().findViewById(R.id.btn_dislike).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dislike) {
            if (onDislikeClickListener != null) onDislikeClickListener.onDislikeClick(v);
            dismiss();
        }
    }

    ////////////////////////////////////////////

    private OnDislikeClickListener onDislikeClickListener;
    private String str;

    public void setOnDislikeClickListener(OnDislikeClickListener onDislikeClickListener) {
        this.onDislikeClickListener = onDislikeClickListener;
    }

    public interface OnDislikeClickListener {
        void onDislikeClick(View v);
    }
}
