package com.wang.social.pictureselector.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by King on 2018/3/30.
 */

public interface ImageEngine {
    void loadImage(Context context, Uri uri, Drawable placeHolder, ImageView imageView);
    void loadImage(Context context, String filepath, Drawable placeHolder, ImageView imageView);
}
