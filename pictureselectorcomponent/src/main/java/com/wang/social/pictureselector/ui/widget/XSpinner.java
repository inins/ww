package com.wang.social.pictureselector.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.ui.adapter.AlbumAdapter;

public class XSpinner extends LinearLayout implements View.OnClickListener {
    public final static String TAG = XSpinner.class.getSimpleName().toString();

    SpinnerPopupWindow mWindow;

    public XSpinner(Context context) {
        this(context, null);
    }

    public XSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 点击事件监听
        setOnClickListener(this);
    }

    public void init(BaseAdapter adapter) {
        if (null == mWindow) {
            mWindow = new SpinnerPopupWindow(getContext(), adapter);
        }

    }

    public void dismiss() {
        if (null != mWindow) {
            mWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (null != mWindow) {
            int[] position = new int[2];
            getLocationOnScreen(position);
//            mWindow.showAsDropDown(this, 0, -getHeight());
            mWindow.showAtLocation(this, Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                    0, position[1]);
        }
    }

    class SpinnerPopupWindow extends PopupWindow {
        public SpinnerPopupWindow(Context context, BaseAdapter adapter) {
            super(context);

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.ps_x_spinner_popup_window,
                    null, false);

            ListView listView = view.findViewById(R.id.list_view);
            listView.setAdapter(adapter);

            setContentView(view);
            setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            setFocusable(true);
            setOutsideTouchable(true);
            update();

            ColorDrawable bg = new ColorDrawable(Color.TRANSPARENT);
            setBackgroundDrawable(bg);
        }
    }
}
