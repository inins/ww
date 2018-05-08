package com.wang.social.moneytree.mvp.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.moneytree.R;

public class DialogCreateGame extends DialogFragment {

    public interface CreateGameCallback {
        void createGame(int resetTime, int peopleNum, int diamond);
    }

    public static DialogCreateGame show(FragmentManager manager, CreateGameCallback callback) {
        DialogCreateGame dialog = new DialogCreateGame();
        dialog.setCreateGameCallback(callback);

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "");

        return dialog;
    }

    // 时间
    private EditText mTimeET;
    // 人数
    private EditText mNumberET;
    // 钻数
    private EditText mPriceET;
    private CreateGameCallback mCreateGameCallback;

    public void setCreateGameCallback(CreateGameCallback createGameCallback) {
        mCreateGameCallback = createGameCallback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.mt_dialog_create_game, null, false);

        mTimeET = view.findViewById(R.id.time_edit_text);
        mNumberET = view.findViewById(R.id.number_of_people_edit_text);
        mPriceET = view.findViewById(R.id.price_text_view);

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 关闭
        view.findViewById(R.id.shutdown_image_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogCreateGame.this.dismiss();
                    }
                });
        // 创建游戏
        view.findViewById(R.id.create_game_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mCreateGameCallback) {
                            createGame();
                        }
                    }
                });
        // 选择时间
        view.findViewById(R.id.time_select_image_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogTimePicker.show(getChildFragmentManager())
                                .setListener(new DialogTimePicker.TimePickerListener() {
                                    @Override
                                    public void onTimePicker(int position) {
                                        setTime(position);
                                    }
                                });
                    }
                });


        return dialog;
    }

    private void setTime(int position) {
        switch (position) {
            case 0:
                mTimeET.requestFocus();
                KeyboardUtils.showSoftInput(mTimeET);
                break;
            case 1:
                mTimeET.setText("1");
                break;
            case 2:
                mTimeET.setText("2");
                break;
            case 3:
                mTimeET.setText("5");
                break;
            case 4:
                mTimeET.setText("10");
                break;
        }

        mTimeET.setSelection(mTimeET.getText().length());
    }

    private void createGame() {
        int time = 0;
        int number = 0;
        int price = 0;

        try {
            time = Integer.parseInt(mTimeET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToastLong("请输入游戏时间");
            return;
        }

        try {
            number = Integer.parseInt(mTimeET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToastLong("请输入游戏人数");
            return;
        }

        try {
            price = Integer.parseInt(mTimeET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToastLong("请输入支付钻石");
            return;
        }

        if (null != mCreateGameCallback) {
            mCreateGameCallback.createGame(time, number, price);
        }
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
