package com.wang.social.funshow.mvp.ui.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.utils.SpannableStringUtil;
import com.wang.social.funshow.R;

public class DialogSureFunshowPay extends DialogSure {
    public DialogSureFunshowPay(Context context, CharSequence title) {
        super(context, title, "再逛逛", "立即支付");
    }

    public static void showDialog(Context context, int stoneCount, OnSureCallback callback) {
        int[] colors = {ContextCompat.getColor(context, R.color.common_text_blank_dark),
                ContextCompat.getColor(context, R.color.common_blue_deep),
                ContextCompat.getColor(context, R.color.common_text_blank_dark)};
        SpannableStringBuilder spannableString = SpannableStringUtil.createV2(new String[]{"查看该趣晒需支付", String.valueOf(stoneCount), "钻"}, colors);

        DialogSureFunshowPay dialogSure = new DialogSureFunshowPay(context, spannableString);
        dialogSure.setOkClickListener((view -> {
            if (callback != null) callback.onOkClick();
            dialogSure.dismiss();
        }));
        dialogSure.show();
    }
}
