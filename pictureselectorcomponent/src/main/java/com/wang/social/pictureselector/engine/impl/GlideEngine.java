package com.wang.social.pictureselector.engine.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wang.social.pictureselector.engine.ImageEngine;

/**
 * Created by King on 2018/3/30.
 */

public class GlideEngine implements ImageEngine {
    @Override
    public void loadImage(Context context, Uri uri, Drawable placeHolder, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, String filepath, Drawable placeHolder, ImageView imageView) {
        Glide.with(context)
                .load(filepath)
                .into(imageView);
    }
}
