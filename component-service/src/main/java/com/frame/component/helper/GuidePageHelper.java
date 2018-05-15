package com.frame.component.helper;

import android.graphics.drawable.shapes.Shape;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.frame.component.service.R;
import com.frame.utils.SizeUtils;

import java.util.ArrayList;

import static com.app.hubert.guide.model.HighLight.Shape.ROUND_RECTANGLE;

public class GuidePageHelper {

    public static Animation getEnterAnimation() {
        Animation enterAnimation = new AlphaAnimation(0f, 1f);
        enterAnimation.setDuration(200);
        enterAnimation.setFillAfter(true);
        return enterAnimation;
    }

    public static Animation getExitAnimation() {
        Animation exitAnimation = new AlphaAnimation(1f, 0f);
        exitAnimation.setDuration(200);
        exitAnimation.setFillAfter(true);
        return exitAnimation;
    }
}
