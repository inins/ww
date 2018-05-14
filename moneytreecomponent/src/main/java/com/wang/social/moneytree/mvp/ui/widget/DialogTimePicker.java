package com.wang.social.moneytree.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.frame.component.view.WheelPicker;
import com.frame.utils.ToastUtil;
import com.wang.social.moneytree.R;

import java.util.ArrayList;
import java.util.List;

public class DialogTimePicker extends DialogFragment {

    public interface TimePickerListener {
        void onTimePicker(int position);
    }

    public static DialogTimePicker show(FragmentManager manager) {
        DialogTimePicker dialog = new DialogTimePicker();

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "");

        return dialog;
    }

    private TimePicker mTimePicker;
    private int mPosition;
    private TimePickerListener mListener;

    public void setListener(TimePickerListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.mt_dialog_time_picker, null, false);

        mTimePicker = view.findViewById(R.id.wheel_picker);

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        window.setAttributes(lp);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 关闭
        view.findViewById(R.id.cancel_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogTimePicker.this.dismiss();
                    }
                });
        // 确定
        view.findViewById(R.id.confirm_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            mListener.onTimePicker(mPosition);
                        }

                        DialogTimePicker.this.dismiss();
                    }
                });

        mPosition = mTimePicker.getCurrentPosition();
        mTimePicker.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener<String>() {
            @Override
            public void onWheelSelected(String item, int position) {
                mPosition = position;
            }
        });

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
