package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.wang.social.topic.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CommentSortPopWindow extends PopupWindow {

    public interface SortChangeListener {
        void onSortChanged(@SortType int sort, String name);
    }

    public final static int SORT_TYPE_UNKNOWN = -1;
    public final static int SORT_TYPE_HOT = 0;
    public final static int SORT_TYPE_TIME = 1;

    @IntDef({SORT_TYPE_UNKNOWN, SORT_TYPE_HOT, SORT_TYPE_TIME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SortType {
    }

    @SortType
    int mSortType = SORT_TYPE_HOT;

    private int mColorNormal;
    private int mColorSelected;
    private TextView mHotTV;
    private TextView mTimeTV;
    SortChangeListener mSortChangeListener;

    public CommentSortPopWindow(Context context) {
        setContentView(LayoutInflater.from(context).inflate(getLayout(), null));

        // 设置SelectPicPopupWindow的View
        this.setContentView(getContentView());
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        initBase(context);
    }

    public void setSortChangeListener(SortChangeListener listener) {
        this.mSortChangeListener = listener;
    }

    public int getLayout() {
        return R.layout.topic_view_comment_sort_window;
    }

    private void resetTextColor() {
        switch (mSortType) {
            case SORT_TYPE_HOT:
                mHotTV.setTextColor(mColorSelected);
                mTimeTV.setTextColor(mColorNormal);
                break;
            case SORT_TYPE_TIME:
                mHotTV.setTextColor(mColorNormal);
                mTimeTV.setTextColor(mColorSelected);
                break;
            default:
                break;
        }
    }

    private void changeSortType(@SortType int type) {
        dismiss();

        if (type == mSortType) return;

        mSortType = type;
        resetTextColor();

        if (null != mSortChangeListener) {
            mSortChangeListener.onSortChanged(mSortType, getTypeName());
        }
    }

    private String getTypeName() {
        switch (mSortType) {
            case SORT_TYPE_HOT:
                return mHotTV.getText().toString();
            case SORT_TYPE_TIME:
                return mTimeTV.getText().toString();
            default:
                break;
        }

        return "";
    }

    public void initBase(Context context) {
        mColorNormal = context.getResources().getColor(R.color.common_text_blank);
        mColorSelected = context.getResources().getColor(R.color.common_blue_deep);

        mHotTV = getContentView().findViewById(R.id.hot_text_view);
        mHotTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSortType(SORT_TYPE_HOT);
            }
        });

        mTimeTV = getContentView().findViewById(R.id.time_text_view);
        mTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSortType(SORT_TYPE_TIME);
            }
        });

        resetTextColor();
    }


}
