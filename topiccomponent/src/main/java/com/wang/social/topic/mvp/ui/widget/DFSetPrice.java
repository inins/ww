package com.wang.social.topic.mvp.ui.widget;

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

import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;

public class DFSetPrice extends DialogFragment {
    public interface PriceCallback {
        void onPrice(int price);
    }

    public static DFSetPrice showDialog(FragmentManager manager, int price, PriceCallback priceCallback) {
        DFSetPrice dialog = new DFSetPrice();

        dialog.setPriceCallback(priceCallback);
        dialog.setPrice(price);

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "");

        return dialog;
    }

    private PriceCallback mPriceCallback;
    private EditText mPriceET;
    private int mPrice;

    public void setPriceCallback(PriceCallback priceCallback) {
        mPriceCallback = priceCallback;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    private void setPriceETText(int price) {
        mPriceET.setText(Integer.toString(price));
        mPriceET.setSelection(mPriceET.getText().length());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.topic_dialog_set_price, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.TopicDialogStyle);
        dialog.setContentView(view);

        dialog.setCancelable(true);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.80), LinearLayout.LayoutParams.WRAP_CONTENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mPriceET = view.findViewById(R.id.price_edittext);
        if (mPrice > 0) {
            setPriceETText(mPrice);
        } else {
            mPriceET.setText("");
        }
        mPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    if (s.toString().startsWith("0")) {
                        mPriceET.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        view.findViewById(R.id.reset_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPriceET.setText("0");
                    }
                });

        view.findViewById(R.id.cancel_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DFSetPrice.this.dismiss();
                    }
                });

        view.findViewById(R.id.ok_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mPriceCallback) {
                            int price = 0;

                            try {
                                price = Integer.parseInt(mPriceET.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            boolean wrong = false;
                            if (price < 10) {
                                wrong = true;
                                mPriceET.setText("10");
                                setPriceETText(10);
                            }
                            if (price > 400000) {
                                wrong = true;
                                setPriceETText(400000);
                            }

                            if (wrong) {
                                ToastUtil.showToastLong("宝石必须是10-40W");

                                return;
                            }

                            mPriceCallback.onPrice(price);

                            DFSetPrice.this.dismiss();
                        }
                    }
                });

        mPriceET.setFocusable(true);
        mPriceET.setFocusableInTouchMode(true);
        mPriceET.requestFocus();

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
