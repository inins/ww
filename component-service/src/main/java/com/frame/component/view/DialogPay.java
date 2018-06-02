package com.frame.component.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.entities.AccountBalance;
import com.frame.component.entities.Topic;
import com.frame.component.helper.CommonHelper;
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
    // 钻石支付
    public final static int TYPE_PAY_DIAMOND = 1;
    // 宝石支付
    public final static int TYPE_PAY_GEM = 2;

    public interface DialogPayCallback {
        void onPay();
    }

    public static DialogPay showPayAddGroup(IView bindView,
                                            FragmentManager manager,
                                            boolean mi,
                                            int price,
                                            int balance,
                                            DialogPayCallback callback) {
        String[] strings = {
                mi ? "加入此趣聊需要支付" : "加入此觅聊需要支付",
                Integer.toString(price),
                "宝石"};
        int[] colors = {
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank),
                ContextCompat.getColor(Utils.getContext(), R.color.common_blue_deep),
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);

        return showPay(
                TYPE_PAY_GEM, // 话题使用宝石支付
                bindView,
                manager,
                titleText,
                "您当前余额为%1d宝石",
                "再逛逛",
                "立即支付",
                "去充值",
                price,
                balance,
                callback);
    }

    public static DialogPay showPayTopic(IView bindView, FragmentManager manager, Topic topic, int balance, DialogPayCallback callback) {
        return showPayTopic(bindView, manager, topic.getRelateMoney(), balance, callback);
    }

    public static DialogPay showPayTopic(IView bindView,
                                         FragmentManager manager,
                                         int price,
                                         int balance,
                                         DialogPayCallback callback) {
        String[] strings = {
                "查看该话题需支付",
                Integer.toString(price),
                "宝石"};
        int[] colors = {
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank),
                ContextCompat.getColor(Utils.getContext(), R.color.common_blue_deep),
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);

        return showPay(
                TYPE_PAY_GEM, // 话题使用宝石支付
                bindView,
                manager,
                titleText,
                "您当前余额为%1d宝石",
                "再逛逛",
                "立即支付",
                "去充值",
                price,
                balance,
                callback);
    }

    public static DialogPay showPayFunshow(IView bindView,
                                           FragmentManager manager,
                                           int price,
                                           int balance,
                                           DialogPayCallback callback) {
        String[] strings = {
                "查看该趣晒需支付",
                Integer.toString(price),
                "宝石"};
        int[] colors = {
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank),
                ContextCompat.getColor(Utils.getContext(), R.color.common_blue_deep),
                ContextCompat.getColor(Utils.getContext(), R.color.common_text_blank)
        };
        SpannableStringBuilder titleText = SpannableStringUtil.createV2(strings, colors);

        return showPay(TYPE_PAY_GEM,
                bindView,
                manager,
                titleText,
                "您当前余额为%1d宝石",
                "再逛逛",
                "立即支付",
                "去充值",
                price,
                balance,
                callback);
    }

    /**
     * 使用钻石支付
     */
    public static DialogPay showPayDiamond(IView bindView,
                                           FragmentManager manager,
                                           SpannableStringBuilder titleText, String hintText,
                                           String cancelText, String payText, String rechargeText,
                                           int price, int balance,
                                           DialogPayCallback callback) {
        return showPay(
                TYPE_PAY_DIAMOND,
                bindView,
                manager,
                titleText,
                hintText,
                cancelText,
                payText,
                rechargeText,
                price,
                balance,
                callback);
    }

    /**
     * 显示付费对话框(自身调用 NetAccountBalanceHelper )
     *
     * @param type         支付类型，宝石或钻石
     * @param bindView     IView
     * @param manager      FramgentManager
     * @param titleText    标题
     * @param hintText     提示 （您当前余额为%1d钻）
     * @param cancelText   取消文字
     * @param payText      支付
     * @param rechargeText 去充值
     * @param price        价格
     * @param callback     回调
     * @Param balance 余额，当余额为 < 0 时，会调用 accountBalance 查询余额
     */
    private static DialogPay showPay(int type, IView bindView,
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
            // 获取钱包信息
            NetAccountBalanceHelper.newInstance()
                    .accountBalance(bindView,
                            new ErrorHandleSubscriber<AccountBalance>() {
                                @Override
                                public void onNext(AccountBalance accountBalance) {
                                    if (type == TYPE_PAY_DIAMOND) {
                                        dialog.setBalance(accountBalance.getAmountDiamond());
                                    } else if (type == TYPE_PAY_GEM) {
                                        dialog.setBalance(accountBalance.getAmountGemstone());
                                    }

                                    dialog.showDialog();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtil.showToastLong(e.getMessage());
                                }
                            },
                            disposable -> bindView.showLoading(),
                            () -> bindView.hideLoading()
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
        cancelText.setOnClickListener(v -> DialogPay.this.dismiss());

        payText.setOnClickListener(v -> {
            if (null != mCallback) {
                if (mPrice > mBalance) {
                    CommonHelper.PersonalHelper.startAccountActivity(getContext());
                } else {
                    mCallback.onPay();
                }
            }

            DialogPay.this.dismiss();
        });

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

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
