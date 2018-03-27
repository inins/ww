package com.frame.http.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.frame.http.imageloader.BaseImageLoaderStrategy;
import com.frame.utils.Preconditions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * =====================================
 * <p>
 * Created by Bo on 2018-03-26.
 * =====================================
 */

public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy<ImageConfigImpl> {

    @Override
    public void loadImage(Context cxt, ImageConfigImpl config) {
        Preconditions.checkNotNull(cxt, "%s cannot be null!", Context.class.getName());
        Preconditions.checkNotNull(config, "%s cannot be null!", ImageConfigImpl.class.getName());
        if (TextUtils.isEmpty(config.getUrl())) {
            throw new NullPointerException("Url is required");
        }
        Preconditions.checkNotNull(config.getImageView(), "ImageView is required");

        GlideRequests requests;
        requests = WangSocialGlide.with(cxt);

        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade());

        switch (config.getCacheStrategy()) {
            case ALL:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case NONE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case DATA:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case SOURCE:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case AUTOMATIC:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }
        if (config.getTransformation() != null){
            glideRequest.transform(config.getTransformation());
        }
        if (config.getPlaceholder() != 0){
            glideRequest.placeholder(config.getPlaceholder());
        }
        if (config.getErrorPic() != 0){
            glideRequest.error(config.getErrorPic());
        }
        if (config.getFallback() != 0){
            glideRequest.fallback(config.getFallback());
        }
        glideRequest.into(config.getImageView());
    }

    @Override
    public void clear(Context cxt, ImageConfigImpl config) {
        Preconditions.checkNotNull(cxt, "%s cannot be null!", Context.class.getName());
        Preconditions.checkNotNull(config, "ImageConfigImpl is required.");

        if (config.getImageViews() != null && config.getImageViews().length > 0){
            for (ImageView imageView : config.getImageViews()){
                WangSocialGlide.get(cxt).getRequestManagerRetriever().get(cxt).clear(imageView);
            }
        }

        if (config.isClearDiskCache()){
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Glide.get(cxt).clearDiskCache();
                        }
                    });
        }

        if (config.isClearMemory()){
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Glide.get(cxt).clearMemory();
                        }
                    });
        }
    }
}