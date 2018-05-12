package com.wang.social.moneytree.mvp.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.frame.component.view.WheelPicker;
import com.frame.utils.SizeUtils;
import com.wang.social.moneytree.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimePicker extends WheelPicker<String> {


    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemMaximumWidthText("00");

        List<String> list = new ArrayList<>();
        list.add(getContext().getString(R.string.mt_time_define));
        list.add(getContext().getString(R.string.mt_time_minute_1));
        list.add(getContext().getString(R.string.mt_time_minute_2));
        list.add(getContext().getString(R.string.mt_time_minute_5));
        list.add(getContext().getString(R.string.mt_time_minute_10));
        setDataList(list);

        setHalfVisibleItemCount(2);
        setTextSize(SizeUtils.sp2px(18));
        setTextColor(getContext().getResources().getColor(R.color.common_text_dark));
        setSelectedItemTextSize(SizeUtils.sp2px(20));
        setSelectedItemTextColor(getContext().getResources().getColor(R.color.common_blue_deep));
        setCurtainBorderColor(Color.parseColor("#E5E5E6"));
        setShowCurtainBorder(false);
        setShowCurtain(false);
        setShowCurtainBorderHorizontal(true);
    }
}
