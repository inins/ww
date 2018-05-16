package com.frame.component.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.entities.AccountBalance;
import com.frame.component.entities.Topic;
import com.frame.component.helper.NetAccountBalanceHelper;
import com.frame.component.service.R;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class DialogPay extends DialogFragment {
    public interface DialogPayCallback {
        void onPay();
    }

    public static DialogPay showPayTopic(IView bindView,
                                         FragmentManager manager,
                                         Topic topic,
                                         int balance,
                                         DialogPayCallback callback) {
        String[] strings = {
                "查看该话题需支付",
                Integer.toString(topic.getRelateMoney()),
                "钻"};
        int[] colors = {
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank),
                ContextCompat.getColor(Utils.getContext(), R.color.common_blue_deep),
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);

        return showPay(bindView,
                manager,
                titleText,
                "您当前余额为%1d钻",
                "再逛逛",
                "立即支付",
                "",
                topic.getRelateMoney(),
                balance,
                callback);
    }

    /**
     * 显示付费对话框(自身调用 NetAccountBalanceHelper )
     * @param bindView IView
     * @param manager FramgentManager
     * @param titleText 标题
     * @param hintText 提示 （您当前余额为%1d钻）
     * @param cancelText 取消文字
     * @param payText 支付
     * @param rechargeText 去充值
     * @param price 价格
     * @Param balance 余额，当余额为 < 0 时，会调用 accountBalance 查询余额
     * @param callback 回调
     * @return
     */
    public static DialogPay showPay(IView bindView,
                                    FragmentManager manager,
                                    SpannableStringBuilder titleText, String hintText,
                                    String cancelText, String payText, String rechargeText,
                                    int price, int balance,
                                    DialogPayCallback callback) {
        DialogPay dialog = new DialogPay();
        dialog.setFragmentManager(manager);
        dialog.setTitleText(titleText);
        dialog.setHintText(hintText);
        dialog.setCancelText(cancelText);
        dialog.setPayText(payText);
        dialog.setRechargeText(rechargeText);
        dialog.setCallback(callback);
        dialog.setPrice(price);
        if (balance < 0) {
            NetAccountBalanceHelper.newInstance()
                    .accountBalance(bindView,
                            new ErrorHandleSubscriber<AccountBalance>() {
                                @Override
                                public void onNext(AccountBalance accountBalance) {
                                    dialog.setBalance(accountBalance.getAmount());
                                    dialog.showDialog();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtil.showToastLong(e.getMessage());
                                }
                            }, new Consumer<Disposable>() {
                                @Override
                                public void accept(Disposable disposable) throws Exception {
                                    bindView.showLoading();
                                }
                            }, new Action() {
                                @Override
                                public void run() throws Exception {
                                    bindView.hideLoading();
                                }
                            }
                    );
        } else {
            dialog.setBalance(balance);
            dialog.showDialog();
        }


        return dialog;
    }

    private void showDialog() {
        if (null == mFragmentManager) return;

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        show(ft, "");
    }

    private FragmentManager mFragmentManager;
    private SpannableStringBuilder mTitleText;
    private String mHintText;
    private String mCancelText;
    private String mPayText;
    private String mRechargeText;
    private DialogPayCallback mCallback;
    // 需要支付的价格
    private int mPrice;
    // 余额
    private int mBalance;
    private TextView mHintTV;


    public void setBalance(int balance) {
        mBalance = balance;
        if (null != mHintTV && !TextUtils.isEmpty(mHintText)) {
            mHintTV.setText(String.format(mHintText, balance));
        }
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void setCallback(DialogPayCallback callback) {
        mCallback = callback;
    }

    public void setTitleText(SpannableStringBuilder titleText) {
        mTitleText = titleText;
    }

    public void setHintText(String hintText) {
        mHintText = hintText;
    }

    public void setCancelText(String cancelText) {
        mCancelText = cancelText;
    }

    public void setPayText(String payText) {
        mPayText = payText;
    }

    public void setRechargeText(String rechargeText) {
        mRechargeText = rechargeText;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_pay, null, false);

        TextView titleTV = view.findViewById(R.id.title_text_view);
        titleTV.setText(mTitleText);

        mHintTV = view.findViewById(R.id.hint_text_view);
        String hint = mHintText;
        try {
            hint = String.format(mHintText, mBalance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHintTV.setText(hint);

        TextView cancelText = view.findViewById(R.id.cancel_text_view);
        cancelText.setText(mCancelText);

        TextView payText = view.findViewById(R.id.pay_text_view);
        if (mPrice > mBalance) {
            // 去充值
            payText.setText(mRechargeText);
        } else {
            payText.setText(mPayText);
        }

        // 取消
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPay.this.dismiss();
            }
        });

        payText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCallback) {
                    if (mPrice > mBalance) {
                        ToastUtil.showToastLong("去充值");
                    } else {
                        mCallback.onPay();
                    }
                }

                DialogPay.this.dismiss();
            }
        });

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCancelable(true);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(
                (int) (display.getWidth() * 0.85),
                LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


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
