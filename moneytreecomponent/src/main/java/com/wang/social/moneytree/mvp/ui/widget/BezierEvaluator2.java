package com.wang.social.moneytree.mvp.ui.widget;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Created by king on 2017/12/15.
 */

public class BezierEvaluator2 implements TypeEvaluator<Point> {
    private Point controlPoint;

    public BezierEvaluator2(Point controlPoint) {
        this.controlPoint = controlPoint;
    }

    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controlPoint.x + t * t * endValue.x);
        int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controlPoint.y + t * t * endValue.y);
        return new Point(x, y);
    }
}
