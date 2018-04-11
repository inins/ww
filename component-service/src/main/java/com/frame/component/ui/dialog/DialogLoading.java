package com.frame.component.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.service.R2;

import butterknife.BindView;


/**
 * 加载弹窗
 */
public class DialogLoading extends BaseDialog {
    @BindView(R2.id.tipTextView)
    TextView tipTextView;
    private String msg;

    private Runnable runnable;
    private Handler handler = new Handler();

    public DialogLoading(Context context) {
        this(context, "正在加载");
    }

    public DialogLoading(Context context, String msg) {
        super(context, R.style.common_LoadingDialog);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected int getView() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void intView(View root) {
    }

    @Override
    protected void intViewOnCreate(View root) {
        tipTextView.setText(msg);
    }

    /**
     * 重写了show() 和 dismiss() 方法，将dimiss延时0.1秒后执行，如果在0.1秒内再次调用了show，则该次dismiss失效
     * 用于解决连续网络请求导致dialog不断显示和隐藏导致动画不连续的问题
     */

    @Override
    public void show() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.show();
    }

    @Override
    public void dismiss() {
        runnable = () -> DialogLoading.super.dismiss();
        handler.postDelayed(runnable, 100);
    }
}
