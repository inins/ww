package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.frame.component.ui.dialog.DialogSure;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;

import butterknife.BindView;


/**
 * liaoinstan
 * 选择性别弹窗
 */
public class DialogBottomPayway extends BaseDialog implements View.OnClickListener {

    public static final int PAYWAY_ALI = 0;
    public static final int PAYWAY_WX = 1;

    @BindView(R2.id.btn_ali)
    TextView btn_ali;
    @BindView(R2.id.btn_wx)
    TextView btn_wx;
    @BindView(R2.id.btn_cancel)
    TextView btn_cancel;

    public DialogBottomPayway(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogBottom();
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected int getView() {
        return R.layout.personal_dialog_choose_payway;
    }

    @Override
    protected void intView(View root) {
        btn_cancel.setOnClickListener(this);
        btn_ali.setOnClickListener(this);
        btn_wx.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ali) {
            if (onPaywaySelectListener != null) onPaywaySelectListener.onPaywaySelect(PAYWAY_ALI);
        } else if (i == R.id.btn_wx) {
            if (onPaywaySelectListener != null) onPaywaySelectListener.onPaywaySelect(PAYWAY_WX);
        } else if (i == R.id.btn_cancel) {
        }
        dismiss();
    }

    ///////////////////////////

    private OnPaywaySelectListener onPaywaySelectListener;

    public void setOnPaywaySelectListener(OnPaywaySelectListener onPaywaySelectListener) {
        this.onPaywaySelectListener = onPaywaySelectListener;
    }

    public interface OnPaywaySelectListener {
        void onPaywaySelect(int payway);
    }

    ////////////////////////////

    public static void showDialog(Context context, OnPaywaySelectListener callback) {
        DialogBottomPayway dialog = new DialogBottomPayway(context);
        dialog.setOnPaywaySelectListener(callback);
        dialog.show();
    }
}
