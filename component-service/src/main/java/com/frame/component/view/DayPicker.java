package com.frame.component.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日期选择
 * Created by ycuwq on 17-12-28.
 */
public class DayPicker extends WheelPicker<String> {

    private int mEndDay;

    private int mSelectedDay;

    private int mYear, mMonth;

    private NumberFormat numberFormat;
    private String unit = "日";

    private OnDaySelectedListener mOnDaySelectedListener;

    public DayPicker(Context context) {
        this(context, null);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemMaximumWidthText("00");

        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);

        mEndDay = Calendar.getInstance().getActualMaximum(Calendar.DATE);
        updateDay();
        mSelectedDay = Calendar.getInstance().get(Calendar.DATE);
        setSelectedDay(mSelectedDay, false);
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
            @Override
            public void onWheelSelected(String item, int position) {
                String substring = !TextUtils.isEmpty(unit) ? item.substring(0, item.indexOf(unit)) : item;
                int itemInt = Integer.parseInt(substring);
                mSelectedDay = itemInt;
                if (mOnDaySelectedListener != null) {
                    mOnDaySelectedListener.onDaySelected(itemInt);
                }
            }
        });
    }


    public void setMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        mEndDay = calendar.getActualMaximum(Calendar.DATE);
        if (mSelectedDay > mEndDay) {
            setSelectedDay(mEndDay, true);
        }
        updateDay();
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public void setSelectedDay(int selectedMonth) {
        setSelectedDay(selectedMonth, true);
    }

    public void setSelectedDay(int selectedDay, boolean smoothScroll) {
        setCurrentPosition(selectedDay - 1, smoothScroll);
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        mOnDaySelectedListener = onDaySelectedListener;
    }

    private void updateDay() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= mEndDay; i++) {
            list.add(numberFormat.format(i) + unit);
        }
        setDataList(list);
    }

    public interface OnDaySelectedListener {
        void onDaySelected(int day);
    }
}
