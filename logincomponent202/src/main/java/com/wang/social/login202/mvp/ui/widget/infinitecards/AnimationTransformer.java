package com.wang.social.login202.mvp.ui.widget.infinitecards;

import android.view.View;

public interface AnimationTransformer {
    void transformAnimation(View view, float fraction, int cardWidth, int cardHeight,
                            int fromPosition, int toPosition);

    void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight,
                                        int fromPosition, int toPosition);
}