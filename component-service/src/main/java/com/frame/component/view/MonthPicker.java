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
 * 月份选择器
 * Created by ycuwq on 17-12-28.
 */
public class MonthPicker extends WheelPicker<String> {

    private int mSelectedMonth;

    private NumberFormat numberFormat;
    private String unit = "月";

    private OnMonthSelectedListener mOnMonthSelectedListener;

    public MonthPicker(Context context) {
        this(context, null);
    }

    public MonthPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
	    setItemMaximumWidthText("00");

        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);

		Calendar.getInstance().clear();
        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateMonth();
        setSelectedMonth(mSelectedMonth, false);
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
	        @Override
	        public void onWheelSelected(String item, int position) {
                String substring = !TextUtils.isEmpty(unit) ? item.substring(0, item.indexOf(unit)) : item;
                int itemInt = Integer.parseInt(substring);
                mSelectedMonth = itemInt;
		        if (mOnMonthSelectedListener != null) {
		        	mOnMonthSelectedListener.onMonthSelected(itemInt);
		        }
	        }
        });
    }

    public void updateMonth() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(numberFormat.format(i) + unit);
        }
        setDataList(list);
    }

    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    public void setSelectedMonth(int selectedMonth) {
        setSelectedMonth(selectedMonth, true);
    }

    public void setSelectedMonth(int selectedMonth, boolean smoothScroll) {

        setCurrentPosition(selectedMonth - 1, smoothScroll);
    }

	public void setOnMonthSelectedListener(OnMonthSelectedListener onMonthSelectedListener) {
		mOnMonthSelectedListener = onMonthSelectedListener;
	}

	public interface OnMonthSelectedListener {
    	void onMonthSelected(int month);
    }

}
