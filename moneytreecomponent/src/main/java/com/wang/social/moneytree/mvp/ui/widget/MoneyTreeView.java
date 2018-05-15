package com.wang.social.moneytree.mvp.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import com.wang.social.moneytree.R;

public class MoneyTreeView extends FrameLayout implements View.OnClickListener {

    ConstraintLayout mTreeView;
    private boolean mAnimPlaying = false;

    public MoneyTreeView(@NonNull Context context) {
        this(context, null);
    }

    public MoneyTreeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyTreeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.mt_view_money_tree_bg, this, true);

        mTreeView = view.findViewById(R.id.tree_layout);

        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startAnim();
    }

    public void startAnim() {
        if (mAnimPlaying) return;
        mAnimPlaying = true;

        ObjectAnimator animator = ObjectAnimator.ofFloat(mTreeView, "rotation",
                0f, -5f, 5f, 0f);
        mTreeView.setPivotX(mTreeView.getMeasuredWidth() / 2);
        mTreeView.setPivotY(mTreeView.getMeasuredHeight());
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(1000);
        animator.start();
        for (int i = 1; i < mTreeView.getChildCount(); i++) {
            if (mTreeView.getChildAt(i).getTag() == null || "diamond".equals(mTreeView.getChildAt(i).getTag())) {
                ObjectAnimator moneyAnimator = ObjectAnimator.ofFloat(mTreeView.getChildAt(i), "rotation",
                        -20f, 20f, -10f, 10f, -5f, 0f);
                //moneyAnimator.setInterpolator(new OvershootInterpolator());
                moneyAnimator.setDuration(1500);

                if (i == mTreeView.getChildCount() - 1) {
                    moneyAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnimPlaying = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }

                moneyAnimator.start();
            }
        }
    }
}
