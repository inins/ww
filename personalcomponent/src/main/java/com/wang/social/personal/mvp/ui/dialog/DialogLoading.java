package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wang.social.personal.R;

import butterknife.BindView;


/**
 * 加载弹窗
 */
public class DialogLoading extends BaseDialog {
    @BindView(R.id.tipTextView)
    TextView tipTextView;
    private String msg;

    public DialogLoading(Context context) {
        this(context, "正在加载");
    }

    public DialogLoading(Context context, String msg) {
        super(context);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected int getView() {
        return R.layout.personal_dialog_loading;
    }

    @Override
    protected void intView(View root) {

    }

    @Override
    protected void intViewOnCreate(View root) {
        tipTextView.setText(msg);
    }
}
