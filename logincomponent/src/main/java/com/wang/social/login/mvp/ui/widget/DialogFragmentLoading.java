package com.wang.social.login.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wang.social.login.R;

public class DialogFragmentLoading extends DialogFragment {
    public final static String TAG = DialogFragmentLoading.class.getSimpleName().toString();

    public static DialogFragmentLoading showDialog(FragmentManager manager, String tag) {
        DialogFragmentLoading dialog = new DialogFragmentLoading();

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, tag);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.login_dialog_loading, null);

        Dialog dialog = new Dialog(getActivity(), R.style.login_LoadingDialogStyle);
        dialog.setContentView(view);

        dialog.setCancelable(false);
        // 点击外部不消失
        dialog.setCanceledOnTouchOutside(false);
        // 返回键不消失
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.80), LinearLayout.LayoutParams.WRAP_CONTENT));


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
