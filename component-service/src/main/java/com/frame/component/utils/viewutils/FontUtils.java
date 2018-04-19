package com.frame.component.utils.viewutils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by liaoinstan on 2016/8/8 0008.
 * 字体工具类，设置粗体、自定义字体等
 */
public class FontUtils {

    //设置粗体
    public static void boldText(TextView textView) {
        if (textView != null) {
            textView.getPaint().setFakeBoldText(true);
        }
    }

    public static void unboldText(TextView textView) {
        if (textView != null) {
            textView.getPaint().setFakeBoldText(false);
        }
    }

    //设置粗体，可以传入view，但只有TextView会生效
    public static void boldText(View view) {
        if (view instanceof TextView) {
            boldText((TextView) view);
        }
    }
    public static void unboldText(View view) {
        if (view instanceof TextView) {
            unboldText((TextView) view);
        }
    }
}
