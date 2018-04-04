package com.wang.social.pictureselector.model;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by King on 2018/3/28.
 */

public class Picture {
    private final long id;
    private final String displayName;
    private final String filePath;


    public Picture(long id, String displayName, String filePath) {
        this.id = id;
        this.displayName = displayName;
        this.filePath = filePath;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFilePath() {
        return filePath;
    }

    public static Picture fromCursor(Cursor cursor) {
        return new Picture(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)),
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
    }
}
