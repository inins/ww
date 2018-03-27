package com.frame.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ResponseErrorListener;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.http.imageloader.BaseImageLoaderStrategy;
import com.frame.http.imageloader.ImageLoader;
import com.frame.integration.ActivityLifecycle;
import com.frame.integration.ActivityLifecycleForRxLifecycle;
import com.frame.integration.FragmentLifecycle;
import com.frame.integration.IRepositoryManager;
import com.frame.integration.RepositoryManager;
import com.frame.integration.cache.Cache;
import com.frame.integration.cache.CacheType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ======================================
 * 提供一些框架内必须的实例 {@link Module}
 * <p>
 * Create by ChenJing on 2018-03-12 15:18
 * ======================================
 */
@Module
public abstract class AppModule {

    @Singleton
    @Provides
    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null) {
            configuration.configGson(application, builder);
        }
        return builder.create();
    }

    @Binds
    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);

    @Singleton
    @Provides
    static Cache<String, Object> provideExtras(Cache.Factory factory) {
        return factory.build(CacheType.EXTRAS_CACHE);
    }

    @Binds
    @Named("ActivityLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle activityLifecycle);

    @Binds
    @Named("ActivityLifecycleForRxLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycleForRxLifecycle(ActivityLifecycleForRxLifecycle rxLifecycle);

    @Binds
    abstract FragmentManager.FragmentLifecycleCallbacks bindFragmentLifecycle(FragmentLifecycle fragmentLifecycle);

    @Singleton
    @Provides
    static List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles() {
        return new ArrayList<>();
    }

    @Singleton
    @Provides
    static RxErrorHandler provideErrorHandler(Application application, ResponseErrorListener responseErrorListener) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(responseErrorListener)
                .build();
    }

    @Singleton
    @Provides
    static ApiHelper provideApiHelper(){
        return new ApiHelper();
    }

    @Singleton
    @Provides
    static ImageLoader provideImageLoader(BaseImageLoaderStrategy strategy){
        return new ImageLoader(strategy);
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }
}
