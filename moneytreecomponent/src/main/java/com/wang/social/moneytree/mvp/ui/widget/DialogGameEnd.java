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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.utils.SpannableStringUtil;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.ui.adapter.GameEndRankingAdapter;

import timber.log.Timber;

public class DialogGameEnd extends DialogFragment {

    public interface GameOverListener {
        void onGameOverDialogDismiss();
    }

    public static DialogGameEnd show(FragmentManager manager, GameEnd gameEnd, GameOverListener listener) {
        DialogGameEnd dialog = new DialogGameEnd();
        dialog.setGameEnd(gameEnd);
        dialog.setGameOverListener(listener);

        FragmentTransaction ft = manager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "");

        return dialog;
    }

    private GameEnd mGameEnd;
    private GameOverListener mGameOverListener;

    public void setGameOverListener(GameOverListener gameOverListener) {
        mGameOverListener = gameOverListener;
    }

    public void setGameEnd(GameEnd gameEnd) {
        mGameEnd = gameEnd;
    }

    private String countTimer(int countTime) {
        final int MINUTE = 60;
        final int HOUR = 60 * 60;
        int hh = (int)countTime / HOUR;
        int mm = (int)countTime % HOUR / MINUTE;
        int ss = countTime % MINUTE;

        return (hh > 0 ? String.format("%02d", hh) + ":" : "") +
                String.format("%02d", mm) + ":" +
                String.format("%02d", ss);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.mt_dialog_game_end, null, false);

        if (null != mGameEnd) {
            // 时间
            TextView timeTV = view.findViewById(R.id.time_text_view);
            timeTV.setText(String.format(getString(R.string.mt_format_game_over_time),
                    countTimer(mGameEnd.getUsedTimeLen())));
            // 参与人数和钻石
            TextView infoTV = view.findViewById(R.id.info_text_view);
            infoTV.setText(String.format(getString(R.string.mt_format_game_over_info),
                    mGameEnd.getJoinNum(), mGameEnd.getDiamond()));
            // 排名和钻石
            TextView myTV = view.findViewById(R.id.my_score_text_view);
            String[] strings = {
                    getString(R.string.mt_game_over_my_score_1),
                    Integer.toString(mGameEnd.getRanking()),
                    String.format(getString(R.string.mt_game_over_my_score_2),
                            mGameEnd.getMyGetDiamond())};
            int[] colors = {
                    ContextCompat.getColor(getContext(), R.color.common_white),
                    0xFFF4FF46,
                    ContextCompat.getColor(getContext(), R.color.common_white)
            };
            SpannableStringBuilder myScoreText = SpannableStringUtil.createV2(strings, colors);
            myTV.setText(myScoreText);
            //  TOP 5
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            LayoutAnimationController layoutAnimationController =
                    AnimationUtils.loadLayoutAnimation(getContext(), R.anim.mt_layout_anim_right_in);
            recyclerView.setLayoutAnimation(layoutAnimationController);
            recyclerView.setAdapter(new GameEndRankingAdapter(recyclerView, mGameEnd.getList()));
        }

        view.findViewById(R.id.shutdown_image_view)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }



    @Override
    public void onStart() {
        super.onStart();
        Timber.i("onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
        if (null != mGameOverListener) {
            mGameOverListener.onGameOverDialogDismiss();
        }
    }
}
