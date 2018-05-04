package com.wang.social.pictureselector.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wang.social.pictureselector.R;
import com.wang.social.pictureselector.model.SelectorSpec;
import com.wang.social.pictureselector.ui.widget.ClipZoomImageView;

/**
 * Created by King on 2018/3/30.
 */

public class PreviewAdapter extends PagerAdapter {

    private Context context;
    private String[] files;

    public PreviewAdapter(Context context, String[] files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String filepath = files[position];

        View view = LayoutInflater.from(context)
                .inflate(R.layout.ps_preview_image_layout, container, false);
        ClipZoomImageView imageView = view.findViewById(R.id.image_view);
        SelectorSpec.getInstance()
                .getImageEngine()
                .loadImage(context, filepath, null, imageView);

        container.addView(view);

        return view;
    }
}
