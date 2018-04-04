package com.wang.social.pictureselector.controller;

import android.database.Cursor;

/**
 * Created by King on 2018/3/28.
 */

public interface IControllerListener {
    void onLoadFinished(Cursor cursor);
    void onLoaderReset();
}
