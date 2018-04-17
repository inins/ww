package com.frame.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.frame.component.service.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 年份选择器
 * Created by ycuwq on 17-12-27.
 */
@SuppressWarnings("unused")
public class YearPicker extends WheelPicker<String> {

    private int mStartYear, mEndYear;
    private int mSelectedYear;
    private String unit = "年";
    private OnYearSelectedListener mOnYearSelectedListener;

    public YearPicker(Context context) {
        this(context, null);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setItemMaximumWidthText("0000");
        updateYear();
        setSelectedYear(mSelectedYear, false);
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
            @Override
            public void onWheelSelected(String item, int position) {
                String substring = !TextUtils.isEmpty(unit) ? item.substring(0, item.indexOf(unit)) : item;
                int itemInt = Integer.parseInt(substring);
                mSelectedYear = itemInt;
                if (mOnYearSelectedListener != null) {
                    mOnYearSelectedListener.onYearSelected(itemInt);
                }
            }
        });
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YearPicker);
        mStartYear = a.getInteger(R.styleable.YearPicker_startYear, 1960);
        mEndYear = a.getInteger(R.styleable.YearPicker_endYear, 2017);
        a.recycle();

    }

    private void updateYear() {
        List<String> list = new ArrayList<>();
        for (int i = mStartYear; i <= mEndYear; i++) {
            list.add(i + unit);
        }
        setDataList(list);
    }

    public void setYear(int startYear, int endYear) {
        mStartYear = startYear;
        mEndYear = endYear;
        updateYear();
    }

    public void setSelectedYear(int selectedYear) {
        setSelectedYear(selectedYear, true);
    }

    public void setSelectedYear(int selectedYear, boolean smoothScroll) {
        mSelectedYear = selectedYear;

        setCurrentPosition(mSelectedYear - mStartYear, smoothScroll);
    }

    public int getSelectedYear() {
        return mSelectedYear;
    }

    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        mOnYearSelectedListener = onYearSelectedListener;
    }

    public interface OnYearSelectedListener {
        void onYearSelected(int year);
    }

}
