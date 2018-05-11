package com.frame.component.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.dialog.BaseDialog;

import butterknife.BindView;

/**
 * 确定  取消   弹出
 * 凡是底部有2个按钮的弹窗都可以继承该类,提供统一的样式
 */
public abstract class BaseDialogOkCancel extends BaseDialog {

    public TextView btnCancel;
    public TextView btnOk;

    private String okBtnText;
    private String cancelBtnText;

    public BaseDialogOkCancel(Context context) {
        this(context, "取消", "确定");
    }

    public BaseDialogOkCancel(Context context, String cancelBtnText, String okBtnText) {
        super(context, R.style.common_MyDialog);
        this.cancelBtnText = cancelBtnText;
        this.okBtnText = okBtnText;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogCenter();
        setSize(0.85f, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected final int getView() {
        return R.layout.common_dialog_okcancel;
    }

    @Override
    protected final void getViewBeforeBind(View root) {
        FrameLayout content = root.findViewById(R.id.content);
        LayoutInflater.from(getContext()).inflate(getContentView(), content, true);
    }

    @Override
    protected void intView(View root) {
        btnCancel = root.findViewById(R.id.btn_cancel);
        btnOk = root.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(view -> dismiss());
        btnOk.setOnClickListener(view -> dismiss());
    }

    @Override
    protected void intViewOnCreate(View root) {
        btnCancel.setText(cancelBtnText);
        btnOk.setText(okBtnText);
    }

    private View.OnClickListener defaultListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

    public void setOkClickListener(View.OnClickListener listener) {
        btnOk.setOnClickListener(listener);
    }

    public void setCancelClickListener(View.OnClickListener listener) {
        btnCancel.setOnClickListener(listener);
    }

    ///////////////////////////

    protected abstract int getContentView();
}
