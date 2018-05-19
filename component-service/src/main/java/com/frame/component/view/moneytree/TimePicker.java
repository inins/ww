package com.frame.component.view.moneytree;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.frame.component.service.R;
import com.frame.component.view.WheelPicker;
import com.frame.utils.SizeUtils;
import java.util.ArrayList;
import java.util.List;

public class TimePicker extends WheelPicker<String> {
    private final static String[] times = {
           "自定义",
            "1分钟",
            "2分钟",
            "5分钟",
            "10分钟"
    };
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
        for(String time : times) {
            list.add(time);
        }
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
