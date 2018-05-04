package com.frame.component.utils;

import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;

import com.frame.utils.Utils;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 14:13
 * ============================================
 */
public class UIUtil {

    /**
     * 根据资源ID获取String
     * @param resId
     * @return
     */
    public static String getString(@StringRes int resId){
        return Utils.getContext().getString(resId);
    }

    /**
     * 根据资源ID获取String
     * @param resId
     * @param format
     * @return
     */
    public static String getString(@StringRes int resId, Object... format){
        return String.format(Utils.getContext().getString(resId), format);
    }

    /**
     * 获取定义的dp值
     * @param resId
     * @return
     */
    public static int getDimen(@DimenRes int resId){
        return Utils.getContext().getResources().getDimensionPixelSize(resId);
    }
}
