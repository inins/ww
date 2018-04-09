package com.wang.social.personal.utils.viewutils;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liaoinstan on 2017/6/29.
 */

public class ViewUtil {
    /**
     * 测量View的宽高
     */
    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    //从布局中查找toolbar，并返回，如果有多个只返回一个，如果没找到，返回null
    public static Toolbar findToolbar(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                findToolbar((ViewGroup) child);
            } else {
                if (child instanceof Toolbar) {
                    return (Toolbar) child;
                } else {
                    continue;
                }
            }
        }
        return null;
    }
}
