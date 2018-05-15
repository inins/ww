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
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.DialogPay;
import com.frame.mvp.IView;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.moneytree.R;

public class DialogCreateGame extends DialogFragment {

    public interface CreateGameCallback {
        void createGame(int resetTime, int peopleNum, boolean unlimitedPeople, int diamond);
    }

    public static DialogCreateGame show(IView iView, FragmentManager manager, CreateGameCallback callback) {
        DialogCreateGame dialog = new DialogCreateGame();
        dialog.setIView(iView);
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
    private TextView mNumberTV;
    // 钻数
    private EditText mPriceET;
    // 不限人数
    private CheckBox mUnlimitedCB;
    private IView mIView;
    private CreateGameCallback mCreateGameCallback;

    private final static int MAX_TIME = 300;
    private final static int MIN_NUM = 2;

    public void setIView(IView IView) {
        mIView = IView;
    }

    public void setCreateGameCallback(CreateGameCallback createGameCallback) {
        mCreateGameCallback = createGameCallback;
    }

    private void setTimeText(int time) {
        mTimeET.setText(Integer.toString(time));
        mTimeET.setSelection(mTimeET.getText().length());
    }

    private void setNumberText(int num) {
        mNumberET.setText(Integer.toString(num));
        mNumberET.setSelection(mNumberET.getText().length());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.mt_dialog_create_game, null, false);

        mTimeET = view.findViewById(R.id.time_edit_text);
        mNumberET = view.findViewById(R.id.number_of_people_edit_text);
        mNumberTV = view.findViewById(R.id.number_of_people_text_view);
        mPriceET = view.findViewById(R.id.price_edit_text);
        mUnlimitedCB = view.findViewById(R.id.unlimited_check_box);

        // 默认值
        setTimeText(10);
        setNumberText(2);

//        mTimeET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int time = 0;
//                try {
//                    time = Integer.parseInt(mTimeET.getText().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if(time > MAX_TIME) {
//                    ToastUtil.toastShort(String.format("游戏时间不超过%1d分钟", MAX_TIME));
//                    setTimeText(MAX_TIME);
//                }
//            }
//        });
//
//        mNumberET.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int number = 0;
//                try {
//                    number = Integer.parseInt(mNumberET.getText().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if(number < MIN_NUM) {
//                    ToastUtil.toastShort(String.format("游戏人数最低%1d人", MIN_NUM));
//                    setNumberText(MIN_NUM);
//                }
//            }
//        });


        mUnlimitedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mNumberET.setVisibility(View.GONE);
                    mNumberTV.setVisibility(View.VISIBLE);
                } else {
                    mNumberET.setVisibility(View.VISIBLE);
                    mNumberTV.setVisibility(View.GONE);
                }
            }
        });

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
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
                            if (checkInput()) {
                                createGame();
                            }
//                            if (checkInput()) {
//                                popupDialogPay();
//                            }
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

    private void popupDialogPay() {

        int price = 0;

        try {
            price = Integer.parseInt(mPriceET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] strings = {
                getString(R.string.mt_format_pay_title_pre),
                Integer.toString(price),
                getString(R.string.mt_format_pay_title_suf)};
        int[] colors = {
                ContextCompat.getColor(getContext(), R.color.common_text_blank),
                ContextCompat.getColor(getContext(), R.color.common_blue_deep),
                ContextCompat.getColor(getContext(), R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);
        DialogPay.showPay(mIView,
                getChildFragmentManager(),
                titleText,
                getString(R.string.mt_format_pay_hint),
                getString(R.string.common_cancel),
                getString(R.string.mt_pay_immediately),
                getString(R.string.mt_recharge_immediately),
                price,
                -1,
                new DialogPay.DialogPayCallback() {

                    @Override
                    public void onPay() {
                        createGame();
                    }
                });
    }

    private void setTime(int position) {
        switch (position) {
            case 0:
                mTimeET.requestFocus();
                KeyboardUtils.showSoftInput(mTimeET);
                break;
            case 1:
//                mTimeET.setText("1");
                setTimeText(1);
                break;
            case 2:
//                mTimeET.setText("2");
                setTimeText(2);
                break;
            case 3:
//                mTimeET.setText("5");
                setTimeText(5);
                break;
            case 4:
//                mTimeET.setText("10");
                setTimeText(10);
                break;
        }

        mTimeET.setSelection(mTimeET.getText().length());
    }

    private int mResetTime;
    private int mPeopleNum;
    private int mPrice;
    private boolean checkInput() {

        try {
            mResetTime = Integer.parseInt(mTimeET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToastLong("请输入游戏时间");
            return false;
        }

        if (mResetTime > MAX_TIME) {
            ToastUtil.showToastLong(String.format("游戏时间不超过%1d分钟", MAX_TIME));
            setTimeText(MAX_TIME);
            return false;
        }

        if (!mUnlimitedCB.isChecked()) {
            try {
                mPeopleNum = Integer.parseInt(mNumberET.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showToastLong("请输入游戏人数");
                return false;
            }

            if (mPeopleNum < MIN_NUM) {
                ToastUtil.showToastLong(String.format("游戏人数最低%1d人", MIN_NUM));
                setNumberText(MIN_NUM);
                return false;
            }
        }

        try {
            mPrice = Integer.parseInt(mPriceET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToastLong("请输入支付钻石");
            return false;
        }

        return true;
    }

    private void createGame() {
        if (null != mCreateGameCallback) {
            // 分钟转为秒
            mCreateGameCallback.createGame(mResetTime * 60, mPeopleNum, mUnlimitedCB.isChecked(), mPrice);
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
