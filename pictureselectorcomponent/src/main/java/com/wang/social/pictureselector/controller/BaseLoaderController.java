package com.wang.social.pictureselector.controller;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;

import java.lang.ref.WeakReference;

/**
 * Created by King on 2018/3/28.
 */

public abstract class BaseLoaderController implements LoaderManager.LoaderCallbacks<Cursor> {
    protected static final int PICTURE_LOADER_ID = 1;
    protected static final int ALBUM_LOADER_ID = 2;

    protected WeakReference<Context> context;
    protected WeakReference<IControllerListener> listener;

    protected LoaderManager loaderManager;

    protected void init(Activity activity, IControllerListener listener) {
        context = new WeakReference<Context>(activity);
        this.listener = new WeakReference<>(listener);
        loaderManager = activity.getLoaderManager();
    }

    public void onDestroy() {
        loaderManager.destroyLoader(getLoaderId());
    }

    protected abstract int getLoaderId();
}
