package com.frame.utils;

import android.view.View;

/**
 * Created by liaoinstan on 2017/6/28.
 */

public class FocusUtil {

    //下面代码使焦点回到view
    public static void focusToTop(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
}
