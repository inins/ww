package com.frame.http.imageloader.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.frame.di.component.AppComponent;
import com.frame.http.OkHttpUrlLoader;
import com.frame.utils.DataHelper;
import com.frame.utils.FrameUtils;

import java.io.File;
import java.io.InputStream;

/**
 * =====================================
 * {@link com.bumptech.glide.module.AppGlideModule}默认实现类
 * 用于配置缓存文件夹，切换图片请求框架，配置内存缓存大小等
 *
 * Created by ChenJing on 2018-03-26.
 * =====================================
 */
@GlideModule(glideName = "WangSocialGlide")
public class GlideConfiguration extends AppGlideModule{
    private static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        AppComponent appComponent = FrameUtils.obtainAppComponentFromContext(context);
        builder.setDiskCache(new DiskCache.Factory() {
            @Nullable
            @Override
            public DiskCache build() {
                return DiskLruCacheWrapper.create(DataHelper.makeDirs(new File(appComponent.cacheFile(), "Glide")), IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //Glide 默认使用 HttpURLConnection做网络请求，框架内切换为OkHttp
//        AppComponent appComponent = FrameUtils.obtainAppComponentFromContext(context);
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(appComponent.okhttpClient()));
    }
}
