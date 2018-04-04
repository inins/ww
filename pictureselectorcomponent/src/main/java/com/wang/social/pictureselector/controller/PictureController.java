package com.wang.social.pictureselector.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.wang.social.pictureselector.loader.PictureLoader;
import com.wang.social.pictureselector.model.Album;

/**
 * Created by King on 2018/3/28.
 */

public class PictureController extends BaseLoaderController {
    private static final String ARGS_ALBUM = "ARGS_ALBUM";

    @Override
    protected int getLoaderId() {
        return PICTURE_LOADER_ID;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Album album = bundle.getParcelable(ARGS_ALBUM);
        if (album == null) {
            return null;
        }

        if (null == context) {
            return null;
        }

        return PictureLoader.newInstance(context.get(), album);
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

    public void init(Activity activity) {
        init(activity, null);
    }

    public void init(Activity activity, IControllerListener listener) {
        super.init(activity, listener);
    }

    public void load(Album album) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ALBUM, album);
        loaderManager.initLoader(getLoaderId(), args, this);
    }

    public void resetLoad(Album target) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ALBUM, target);
        loaderManager.restartLoader(getLoaderId(), args, this);
    }


    public void loadAllPhoto(Context context) {
        Album album = new Album(Album.ALBUM_ID_ALL,
                context.getString(Album.ALBUM_NAME_ALL_RES_ID), 0);
        load(album);
    }
}
