package com.frame.component.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-04 16:00
 * ============================================
 */
public class PayDialog extends BaseDialog {

    @BindView(R2.id.dp_tv_message)
    TextView dpTvMessage;
    @BindView(R2.id.dp_tv_cancel)
    TextView dpTvCancel;
    @BindView(R2.id.dp_tv_pay)
    TextView dpTvPay;

    private OnPayListener mPayListener;

    SpannableString messageSpan;

    public PayDialog(Context context, OnPayListener payListener, String message, String price) {
        super(context);
        mPayListener = payListener;

        messageSpan = new SpannableString(message);
        int start = message.indexOf(price);
        int end = start + price.length();
        if (start > 0 && start < message.length() && end < message.length()) {
            messageSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.common_colorAccent)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dpTvMessage.setText(messageSpan);

        Window win = this.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (ScreenUtils.getScreenWidth() * 0.85f);
        lp.height = SizeUtils.dp2px(135f);
        win.setAttributes(lp);
    }

    @Override
    protected int getView() {
        return R.layout.common_dialog_pay;
    }

    @Override
    protected void intView(View root) {
    }

    @OnClick({R2.id.dp_tv_cancel, R2.id.dp_tv_pay})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.dp_tv_pay) {
            if (mPayListener != null) {
                mPayListener.onPay();
            }
        }
        dismiss();
    }

    public interface OnPayListener {

        void onPay();
    }
}
