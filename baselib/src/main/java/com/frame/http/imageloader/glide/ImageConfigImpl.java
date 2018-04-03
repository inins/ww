package com.frame.http.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.frame.http.imageloader.ImageConfig;

/**
 * Created by Bo on 2018-03-26.
 */

public class ImageConfigImpl extends ImageConfig{

    private DiskCacheStrategy cacheStrategy;
    private int fallback; //请求 url 为空,则使用此图片作为占位符
    private BitmapTransformation transformation;//glide用它来改变图形的形状
    private boolean isCircle;
    private ImageView[] imageViews;
    private boolean isClearMemory;//清理内存缓存
    private boolean isClearDiskCache;//清理本地缓存

    private ImageConfigImpl(Builder builder) {
        this.url = builder.url;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.fallback = builder.fallback;
        this.cacheStrategy = builder.cacheStrategy;
        this.transformation = builder.transformation;
        this.isCircle = builder.isCircle;
        this.imageViews = builder.imageViews;
        this.isClearMemory = builder.isClearMemory;
        this.isClearDiskCache = builder.isClearDiskCache;
    }

    public DiskCacheStrategy getCacheStrategy() {
        return cacheStrategy;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public boolean isClearMemory() {
        return isClearMemory;
    }

    public boolean isClearDiskCache() {
        return isClearDiskCache;
    }

    public int getFallback() {
        return fallback;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String url;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private int fallback; //请求 url 为空,则使用此图片作为占位符
        private DiskCacheStrategy cacheStrategy = DiskCacheStrategy.ALL;
        private BitmapTransformation transformation;//glide用它来改变图形的形状
        private boolean isCircle;
        private ImageView[] imageViews;
        private boolean isClearMemory;//清理内存缓存
        private boolean isClearDiskCache;//清理本地缓存

        private Builder() {
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Builder fallback(int fallback) {
            this.fallback = fallback;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder cacheStrategy(DiskCacheStrategy cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Builder transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Builder imageViews(ImageView... imageViews) {
            this.imageViews = imageViews;
            return this;
        }

        public Builder isClearMemory(boolean isClearMemory) {
            this.isClearMemory = isClearMemory;
            return this;
        }

        public Builder isClearDiskCache(boolean isClearDiskCache) {
            this.isClearDiskCache = isClearDiskCache;
            return this;
        }

        public Builder isCircle(boolean isCircle){
            this.isCircle = isCircle;
            return this;
        }

        public ImageConfigImpl build() {
            return new ImageConfigImpl(this);
        }
    }

    public enum DiskCacheStrategy{
        ALL,NONE, SOURCE, DATA, AUTOMATIC
    }
}
