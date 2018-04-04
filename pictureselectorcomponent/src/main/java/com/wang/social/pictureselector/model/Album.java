package com.wang.social.pictureselector.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.StringRes;

import com.wang.social.pictureselector.R;

/**
 * Created by King on 2018/3/28.
 *
 * 相册，有图片的文件夹
 */

public class Album implements Parcelable{
    // 所有图片
    public static final String ALBUM_ID_ALL = String.valueOf(-1);
    public @StringRes static final int ALBUM_NAME_ALL_RES_ID = R.string.ps_all_pictures;

    // id
    private final String id;
    // 显示名称
    private final String displayName;
    // 文件夹内有多少张图片
    private final long count;

    public Album(String id, String displayName, long count) {
        this.id = id;
        this.displayName = displayName;
        this.count = count;
    }

    public static Album valueOf(Cursor cursor) {
        return new Album(
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)),
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)),
                cursor.getLong(3));
    }

    public String getId() {
        return id;
    }

    /**
     * 是否多有图片
     * @return true if all
     */
    public boolean isAll() {
        return ALBUM_ID_ALL.equals(id);
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getCount() {
        return count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.displayName);
        parcel.writeLong(this.count);
    }

    private Album(Parcel in) {
        this.id = in.readString();
        this.displayName = in.readString();
        this.count = in.readLong();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {

        @Override
        public Album createFromParcel(Parcel parcel) {
            return new Album(parcel);
        }

        @Override
        public Album[] newArray(int i) {
            return new Album[i];
        }
    };
}
