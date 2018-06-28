package com.frame.component.ui.dialog;

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
import com.frame.component.view.DialogPay;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;

public class DialogOkCancel extends DialogFragment {

    public interface DialogOkCancelCallback {
        void onOk();
        void onCancel();
    }

    /**
     * 确定 取消 对话框
     * @param manager FragmentManager
     * @param titleText 标题
     * @param hintText 提示文字
     * @param cancelText 取消文字
     * @param okText 确认文字
     * @param callback 回调
     */
    public static DialogOkCancel show(FragmentManager manager,
                                     SpannableStringBuilder titleText, String hintText,
                                     String cancelText, String okText,
                                       DialogOkCancelCallback callback) {
        DialogOkCancel dialog = new DialogOkCancel();
        dialog.setFragmentManager(manager);
        dialog.setTitleText(titleText);
        dialog.setHintText(hintText);
        dialog.setCancelText(cancelText);
        dialog.setOkText(okText);
        dialog.setCallback(callback);
        dialog.showDialog();

        return dialog;
    }

    private FragmentManager mFragmentManager;
    private SpannableStringBuilder mTitleText;
    private String mHintText;
    private String mCancelText;
    private String mOkText;
    private DialogOkCancelCallback mCallback;


    private void showDialog() {
        if (null == mFragmentManager) return;

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        show(ft, "");
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void setCallback(DialogOkCancelCallback callback) {
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

    public void setOkText(String okText) {
        mOkText = okText;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_ok_cancel, null, false);

        TextView titleTV = view.findViewById(R.id.title_text_view);
        if (TextUtils.isEmpty(mTitleText)) {
            titleTV.setVisibility(View.GONE);
        } else {
            titleTV.setText(mTitleText);
        }

        TextView hintTV = view.findViewById(R.id.hint_text_view);
        if (TextUtils.isEmpty(mHintText)) {
            hintTV.setVisibility(View.GONE);
        } else {
            hintTV.setText(mHintText);
        }

        TextView cancelText = view.findViewById(R.id.cancel_text_view);
        cancelText.setText(mCancelText);

        TextView okTV = view.findViewById(R.id.pay_text_view);
        okTV.setText(mOkText);

        // 取消
        cancelText.setOnClickListener(v -> {
            DialogOkCancel.this.dismiss();

            if (null != mCallback) {
                mCallback.onCancel();
            }
        });

        okTV.setOnClickListener(v -> {
            if (null != mCallback) {
                mCallback.onOk();
            }

            DialogOkCancel.this.dismiss();
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
