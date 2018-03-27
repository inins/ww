package com.frame.http.imageloader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-16 13:35
 * ========================================
 */
public final class ImageLoader {

    BaseImageLoaderStrategy mStrategy;

    public ImageLoader(BaseImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }

    /**
     * 加载图片
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void loadImage(Context context, T config){
        this.mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     * @param context
     * @param config
     * @param <T>
     */
    public <T extends ImageConfig> void clear(Context context, T config){
        this.mStrategy.clear(context, config);
    }

    public void setLoadImageStrategy(BaseImageLoaderStrategy imageStrategy){
        this.mStrategy = imageStrategy;
    }

    public BaseImageLoaderStrategy getLoadImageStrategy(){
        return this.mStrategy;
    }
}
