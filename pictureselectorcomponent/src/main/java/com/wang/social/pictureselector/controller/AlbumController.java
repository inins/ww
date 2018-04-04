package com.wang.social.pictureselector.controller;

import android.app.Activity;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.wang.social.pictureselector.loader.AlbumLoader;
import com.wang.social.pictureselector.ui.adapter.AlbumAdapter;

/**
 * Created by King on 2018/3/28.
 */

public class AlbumController extends BaseLoaderController {
    public final static String TAG = AlbumController.class.getSimpleName().toString();

    private AlbumAdapter albumAdapter;

    public void init(Activity activity) {
        init(activity, null);
    }

    public void init(Activity activity, IControllerListener listener) {
        super.init(activity, listener);

        albumAdapter = new AlbumAdapter(activity, null);
    }

    public void loadAlbums() {
        loaderManager.initLoader(getLoaderId(), null, this);
    }

    @Override
    protected int getLoaderId() {
        return ALBUM_LOADER_ID;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (null == context) {
            return null;
        }

        return AlbumLoader.newInstance(context.get());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (null != listener) {
            listener.get().onLoadFinished(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (null != listener) {
            listener.get().onLoaderReset();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
