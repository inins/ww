package com.wang.social.topic.mvp.ui.widget;

import android.support.design.widget.AppBarLayout;

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    public final static String TAG = AppBarStateChangeListener.class.getSimpleName();

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        float rate = (float)Math.abs(i) / (float)appBarLayout.getTotalScrollRange();

        onRateChanged(rate);

        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onRateChanged(float rate);
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
