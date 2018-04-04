package com.wang.social.login.mvp.ui.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.wang.social.login.R;

/**
 * Created by king on 2017/12/22.
 */

public class DialogLoading extends ProgressDialog {
    public DialogLoading(Context context) {
        this(context, 0);
    }

    public DialogLoading(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        init(getContext());
    }

    private void init(Context context) {
        // 设置不可取消，点击其他区域不能取消
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.login_dialog_loading);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
}
