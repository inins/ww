package com.frame.http.imageloader;

import android.content.Context;

/**
 * =========================================
 * 图片加载策略
 * <p>
 * Create by ChenJing on 2018-03-16 13:27
 * =========================================
 */

public interface BaseImageLoaderStrategy<T extends ImageConfig> {

    /**
     * 加载图片
     * @param cxt
     * @param config
     */
    void loadImage(Context cxt, T config);

    /**
     * 停止加载
     * @param cxt
     * @param config
     */
    void clear(Context cxt, T config);
}
