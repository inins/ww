package com.wang.social.pictureselector.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.provider.MediaStore;

import com.wang.social.pictureselector.model.Album;

/**
 * Created by King on 2018/3/28.
 */

public class PictureLoader extends CursorLoader {
    private static final String[] PROJECTION = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,};
    private static final String ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";
    private static final String SELECTION_SIZE = MediaStore.Images.Media.SIZE + " > ? or "
            + MediaStore.Images.Media.SIZE + " is null";

    public PictureLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    /**
     * 通过相册查找
     *
     * @param context
     * @param album
     * @return
     */
    public static CursorLoader newInstance(Context context, Album album) {
        return newInstance(context, album, 0);
    }

    /**
     * 限定图片大小的查找
     *
     * @param context
     * @param album
     * @param minSize The minimum size of the file in bytes
     * @return
     */
    public static CursorLoader newInstance(Context context, Album album, long minSize) {
        if (album == null || album.isAll()) {
            return new PictureLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                    SELECTION_SIZE, new String[] {minSize + ""}, ORDER_BY);
        }
        return new PictureLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                MediaStore.Images.Media.BUCKET_ID + " = ? and (" + SELECTION_SIZE + ")", new String[] {
                album.getId(), minSize + ""}, ORDER_BY);
    }
}
