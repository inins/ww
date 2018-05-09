package com.wang.social.im.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.Guideline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.UIMessage;

import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-09 17:13
 * ============================================
 */
public class MessageHandlePopup extends PopupWindow {

    @Setter
    private OnHandleListener handleListener;

    private UIMessage uiMessage;

    public MessageHandlePopup(Context context, boolean showWithdraw, UIMessage uiMessage, boolean showTop) {
        super(context);
        this.uiMessage = uiMessage;

        init(context, showWithdraw);
        initView(context, showWithdraw, showTop);
    }

    private void init(Context context, boolean showWithdraw) {
        if (showWithdraw){
            this.setWidth(context.getResources().getDimensionPixelSize(R.dimen.im_msg_handle_popup_width));
        }else {
            this.setWidth(context.getResources().getDimensionPixelSize(R.dimen.im_msg_handle_popup_width_narrow));
        }
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    private void initView(Context context, boolean showWithdraw, boolean showTop) {
        View view = LayoutInflater.from(context).inflate(R.layout.im_message_handle, null);
        TextView tvDelete = view.findViewById(R.id.mh_tv_delete);
        View vLine = view.findViewById(R.id.mh_line);
        TextView tvWithdraw = view.findViewById(R.id.mh_tv_withdraw);
        Guideline guideline = view.findViewById(R.id.mh_guideline);

        if (!showTop){
            view.setBackgroundResource(R.drawable.im_bg_message_handle_bottom);
        }

        if (!showWithdraw) {
            tvWithdraw.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);

            guideline.setGuidelinePercent(1);
        }
        setContentView(view);

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (handleListener != null) {
                    handleListener.onDelete(uiMessage);
                }
            }
        });

        tvWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (handleListener != null) {
                    handleListener.onWithdraw(uiMessage);
                }
            }
        });
    }

    public interface OnHandleListener {

        void onDelete(UIMessage uiMessage);

        void onWithdraw(UIMessage uiMessage);
    }
}
