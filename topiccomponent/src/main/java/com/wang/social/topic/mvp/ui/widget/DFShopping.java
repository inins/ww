package com.wang.social.topic.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wang.social.topic.R;
import com.wang.social.topic.mvp.model.entities.Topic;

public class DFShopping extends DialogFragment {
    public static DFShopping showDialog(FragmentManager manager, Topic topic) {
        if (null == topic) return null;

        DFShopping dialog = new DFShopping();

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, topic.getTitle());

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.topic_dialog_shopping, null, false);

        Dialog dialog = new Dialog(getActivity(), R.style.TopicDialogStyle);
        dialog.setContentView(view);

        dialog.setCancelable(true);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.80), LinearLayout.LayoutParams.WRAP_CONTENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view.findViewById(R.id.cancel_text_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DFShopping.this.dismiss();
                    }
                });

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
