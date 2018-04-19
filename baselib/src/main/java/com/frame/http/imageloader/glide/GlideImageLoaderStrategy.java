package com.frame.http.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
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
//        if (TextUtils.isEmpty(config.getUrl())) {
//            throw new NullPointerException("Url is required");
//        }
        Preconditions.checkNotNull(config.getImageView(), "ImageView is required");

        GlideRequests requests;
        requests = WangSocialGlide.with(cxt);

        GlideRequest<Drawable> glideRequest = requests.load(config.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade());

        RequestOptions requestOptions = new RequestOptions();
        switch (config.getCacheStrategy()) {
            case ALL:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case NONE:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case DATA:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case SOURCE:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case AUTOMATIC:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }
        if (config.getTransformation() != null){
            requestOptions = requestOptions.transform(config.getTransformation());
        }else if (config.isCircle()){
            requestOptions = requestOptions.circleCrop();
        }
        if (config.getPlaceholder() != 0){
            requestOptions = requestOptions.placeholder(config.getPlaceholder());
        }
        if (config.getErrorPic() != 0){
            requestOptions = requestOptions.error(config.getErrorPic());
        }
        if (config.getFallback() != 0){
            requestOptions = requestOptions.fallback(config.getFallback());
        }
        glideRequest
                .apply(requestOptions)
                .into(config.getImageView());
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