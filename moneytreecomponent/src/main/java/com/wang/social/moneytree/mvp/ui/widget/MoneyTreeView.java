package com.wang.social.moneytree.mvp.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.wang.social.moneytree.R;

import java.util.Random;

import timber.log.Timber;

public class MoneyTreeView extends FrameLayout implements View.OnClickListener {

    public interface AnimCallback {
        void onAnimEnd();
    }

    private ConstraintLayout mTreeView;
    private ImageView mGroundDiamondIV;
    private boolean mAnimPlaying = false;
    private AnimCallback mAnimCallback;
    private boolean mFirstClick = true;
    private boolean mCanFly = false;


    private SoundPool mSoundPool;
    private int mSoundActionId;

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

    public void setAnimCallback(AnimCallback animCallback) {
        mAnimCallback = animCallback;
    }

    public void showGroundDiamond(boolean visible) {
        mGroundDiamondIV.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public void setCanFly(boolean canFly) {
        mCanFly = canFly;
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.mt_view_money_tree_bg, this, true);

        mTreeView = view.findViewById(R.id.tree_layout);
        mGroundDiamondIV = view.findViewById(R.id.ground_diamond_image_view);
        mGroundDiamondIV.setVisibility(INVISIBLE);

        setOnClickListener(this);


        mSoundPool = new SoundPool(3,AudioManager.STREAM_MUSIC, 0);
        mSoundActionId = mSoundPool.load(getContext(), R.raw.action, 1);
    }

    public void setFirstClick(boolean firstClick) {
        mFirstClick = firstClick;
    }

    @Override
    public void onClick(View view) {
        startAnim();
    }

    private void flyDiamond(ImageView v) {
        if (!mFirstClick) return;
        mFirstClick = false;
        if (!mCanFly) return;

        // 播放音效
        mSoundPool.play(mSoundActionId, 0.8f, 0.8f, 1, 0, 1);

        FlyDiamond diamond = new FlyDiamond(getContext());
        diamond.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        diamond.setImageDrawable(v.getDrawable());
        diamond.setX(v.getX() + v.getWidth());
        diamond.setY(v.getY());
        diamond.set((int)mGroundDiamondIV.getX(), (int)mGroundDiamondIV.getY());
        addView(diamond);
        diamond.startFly();
        diamond.setListener(flyDiamond -> {
            removeView(diamond);
            mGroundDiamondIV.setVisibility(VISIBLE);
        });
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

        int diamondCount = 0;
        for (int i = 1; i < mTreeView.getChildCount(); i++) {
            if (mTreeView.getChildAt(i).getTag() != null && mTreeView.getChildAt(i).getTag().equals("diamond")) {
                diamondCount++;
            }
        }

        int diamondIndex = new Random().nextInt(diamondCount) + 1;

        Timber.i("钻石 " + diamondIndex);

        for (int i = 1; i < mTreeView.getChildCount(); i++) {
            if (mTreeView.getChildAt(i).getTag() == null ||
                    "diamond".equals(mTreeView.getChildAt(i).getTag())) {
                ObjectAnimator moneyAnimator = ObjectAnimator.ofFloat(mTreeView.getChildAt(i), "rotation",
                        -20f, 20f, -10f, 10f, -5f, 0f);
                //moneyAnimator.setInterpolator(new OvershootInterpolator());
                moneyAnimator.setDuration(1500);

                if (i == diamondIndex) {
                    flyDiamond((ImageView)mTreeView.getChildAt(i));
                }

                if (i == mTreeView.getChildCount() - 1) {
                    moneyAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnimPlaying = false;

                            if (null != mAnimCallback) {
                                mAnimCallback.onAnimEnd();
                            }
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (null != mSoundPool) {
            mSoundPool.release();
        }
    }
}
