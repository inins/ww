package com.wang.social.personal.mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wang.social.personal.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * liaoinstan
 * 弹出窗鸡肋
 */
public abstract class BaseDialog extends Dialog {

    private View root;

    public BaseDialog(Context context) {
        this(context, R.style.common_PopupDialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        setMsgDialog();
    }

    private void setMsgDialog() {
        root = LayoutInflater.from(getContext()).inflate(getView(), null);
        getViewBeforeBind(root);
        ButterKnife.bind(this, root);

        intView(root);

        this.setCanceledOnTouchOutside(true);    //默认点击外部关闭
        super.setContentView(root);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intViewOnCreate(root);
    }

    //设置弹出从底部弹出
    protected void setDialogBottom() {
        Window win = this.getWindow();
        win.setGravity(Gravity.BOTTOM);    //从下方弹出
        win.getAttributes().windowAnimations = R.style.common_AnimBottom;
    }

    protected void setDialogCenter() {
        Window win = this.getWindow();
        win.setGravity(Gravity.CENTER);
    }

    protected void setSize(float widthP, float hightP) {
        Window win = this.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        if (widthP == WindowManager.LayoutParams.MATCH_PARENT) {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (widthP == WindowManager.LayoutParams.WRAP_CONTENT) {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = (int) (screenWidth * widthP); // 宽度
        }
        if (hightP == WindowManager.LayoutParams.MATCH_PARENT) {
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else if (hightP == WindowManager.LayoutParams.WRAP_CONTENT) {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = (int) (lp.width * 0.65); // 高度
        }
        win.setAttributes(lp);
    }

    /////////////////////////

    protected void getViewBeforeBind(View root) {
    }

    protected void intViewOnCreate(View root) {
    }

    protected abstract int getView();

    protected abstract void intView(View root);
}
