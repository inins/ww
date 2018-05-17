package com.wang.social.moneytree.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.ui.adapter.GameEndRankingAdapter;

import timber.log.Timber;

public class DialogShaked extends DialogFragment {
    public interface Callback {
        void onResume();
        void onDestroy();
    }

    private Callback mCallback;

    public static DialogShaked show(FragmentManager manager) {
        DialogShaked dialog = new DialogShaked();

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "");

        return dialog;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.mt_dialog_shaked, null, false);

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(
                (int)(display.getWidth() * 0.85), SizeUtils.dp2px(151)));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mCallback) {
            mCallback.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mCallback) {
            mCallback.onDestroy();
        }
    }
}
