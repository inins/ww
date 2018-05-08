package com.frame.component.ui.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.frame.component.entities.AutoPopupItemModel;
import com.frame.component.service.R;
import com.frame.utils.SizeUtils;

import java.util.List;

/**
 * Created by CJ on 2017/2/4 0004.
 */

public class AutoPopupWindow extends PopupWindow {

    public static final int POINT_TO_LEFT = 1;
    public static final int POINT_TO_RIGHT = 2;

    private LinearLayout llContainer;

    private OnItemClickListener itemClickListener;

    private int pointTo;

    public AutoPopupWindow(@NonNull Context context, List<AutoPopupItemModel> items) {
        this(context, items, POINT_TO_LEFT);
    }

    public AutoPopupWindow(@NonNull Context context, List<AutoPopupItemModel> items, int pointTo) {
        super();

        this.pointTo = pointTo;

        init(context);
        initView(context);
        showList(context, items);
    }

    private void init(Context context) {
        this.setWidth(context.getResources().getDimensionPixelSize(R.dimen.popup_auto_width));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
//        this.setAnimationStyle(R.style.PopAnimation);
        //要设置背景才能点外部消失
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initView(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_auto, null);
        llContainer = (LinearLayout) contentView.findViewById(R.id.pa_ll_container);
        if (pointTo == POINT_TO_LEFT) {
            llContainer.setBackgroundResource(R.drawable.bg_auto_popup);
        } else {
            llContainer.setBackgroundResource(R.drawable.bg_home_popup);
        }
        setContentView(contentView);
    }

    private void showList(Context context, List<AutoPopupItemModel> items) {
        for (int i = 0; i < items.size(); i++) {
            if (i != 0) {
                View line = new View(context);
                line.setBackgroundColor(0xffe5e5e5);
                LinearLayout.LayoutParams lineLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                lineLP.leftMargin = SizeUtils.dp2px(6);
                lineLP.rightMargin = SizeUtils.dp2px(6);
                line.setLayoutParams(lineLP);
                llContainer.addView(line);
            }
            final AutoPopupItemModel item = items.get(i);
            TextView tvMenu = (TextView) LayoutInflater.from(context).inflate(R.layout.popup_auto_item, null);
            tvMenu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            if (item.getIcon() != 0) {
                Drawable iconDrawable = ContextCompat.getDrawable(context, item.getIcon());
                iconDrawable.setBounds(0, 0, iconDrawable.getMinimumWidth(), iconDrawable.getMinimumHeight());
                tvMenu.setCompoundDrawables(iconDrawable, null, null, null);
            }
            tvMenu.setText(item.getMenuName());
            if (i == 0) {
                tvMenu.setBackgroundResource(R.drawable.bg_popup_item_top);
            } else if (i == items.size() - 1) {
                tvMenu.setBackgroundResource(R.drawable.bg_popup_item_bottom);
            } else {
                tvMenu.setBackgroundResource(R.drawable.bg_popup_item);
            }
            LinearLayout.LayoutParams lineLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvMenu.setLayoutParams(lineLP);
            tvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != itemClickListener) {
                        itemClickListener.onItemClick(AutoPopupWindow.this, item.getMenuName());
                    }
                }
            });
            llContainer.addView(tvMenu);
        }
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    public void setWindowBackground(int background) {
        llContainer.setBackgroundResource(background);
    }

    public interface OnItemClickListener {
        void onItemClick(AutoPopupWindow popupWindow, int resId);
    }
}