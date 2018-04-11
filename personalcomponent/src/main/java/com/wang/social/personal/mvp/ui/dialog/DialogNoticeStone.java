package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.frame.component.ui.dialog.BaseDialog;
import com.wang.social.personal.R;

/**
 * 宝石提醒弹窗
 */
public class DialogNoticeStone extends BaseDialog {

    public DialogNoticeStone(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSize(0.8f, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected int getView() {
        return R.layout.personal_dialog_notice_stone;
    }

    @Override
    protected void intView(View root) {
    }

    public static DialogNoticeStone newDialog(Context context) {
        return new DialogNoticeStone(context);
    }
}
