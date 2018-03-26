package com.frame.di.module;

import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.frame.http.BaseUrl;
import com.frame.http.GlobalHttpHandler;
import com.frame.http.api.error.ResponseErrorListener;
import com.frame.http.imageloader.BaseImageLoaderStrategy;
import com.frame.http.imageloader.glide.GlideImageLoaderStrategy;
import com.frame.http.log.DefaultFormatPrinter;
import com.frame.http.log.FormatPrinter;
import com.frame.http.log.RequestInterceptor;
import com.frame.integration.cache.Cache;
import com.frame.integration.cache.LruCache;
import com.frame.utils.DataHelper;
import com.frame.utils.Preconditions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * ======================================
 * 可向框架中注入外部配置的自定义参数
 * <p>
 * Create by ChenJing on 2018-03-12 15:31
 * ======================================
 */
@Module
public class GlobalConfigModule {

    private HttpUrl mApiUrl;
    private BaseUrl mBaseUrl;
    private BaseImageLoaderStrategy mLoaderStrategy;
    private GlobalHttpHandler mHandler;
    private ResponseErrorListener mResponseErrorListener;
    private List<Interceptor> mInterceptors;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkHttpConfiguration mOkHttpConfiguration;
    private AppModule.GsonConfiguration mGsonConfiguration;
    private RequestInterceptor.Level mPrintHttpLogLevel;
    private FormatPrinter mFormatPrinter;
    private Cache.Factory mCacheFactory;

    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mBaseUrl = builder.baseUrl;
        this.mLoaderStrategy = builder.loaderStrategy;
        this.mHandler = builder.httpHandler;
        this.mResponseErrorListener = builder.responseErrorListener;
        this.mInterceptors = builder.interceptors;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkHttpConfiguration = builder.okHttpConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
        this.mFormatPrinter = builder.formatPrinter;
        this.mCacheFactory = builder.cacheFactory;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors(){
        return mInterceptors;
    }

    @Singleton
    @Provides
    HttpUrl provideBaseUrl(){
        if (mBaseUrl != null){
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null){
                return httpUrl;
            }
        }
        Preconditions.checkNotNull(mApiUrl, "%s cannot be null!", HttpUrl.class.getName());
        return mApiUrl;
    }

    @Singleton
    @Provides
    BaseImageLoaderStrategy provideImageLoaderStrategy(){
        return mLoaderStrategy == null ? new GlideImageLoaderStrategy() : mLoaderStrategy;
    }

    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler(){
        return mHandler == null ? GlobalHttpHandler.EMPTY : mHandler;
    }

    @Singleton
    @Provides
    ResponseErrorListener provideResponseErrorListener(){
        return mResponseErrorListener == null ? ResponseErrorListener.EMPTY : mResponseErrorListener;
    }

    @Singleton
    @Provides
    File provideCacheFile(Application application){
        return mCacheFile == null ? DataHelper.getCacheFile(application) : mCacheFile;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration(){
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkHttpConfiguration provideOkHttpConfiguration(){
        return mOkHttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.GsonConfiguration provideGsonConfiguration(){
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    RequestInterceptor.Level providePrintHttpLogLevel(){
        return mPrintHttpLogLevel == null ? RequestInterceptor.Level.ALL : mPrintHttpLogLevel;
    }

    @Singleton
    @Provides
    FormatPrinter provideFormatPrinter(){
        return mFormatPrinter == null ? new DefaultFormatPrinter() : mFormatPrinter;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(Application application){
        return mCacheFactory == null ? cacheType -> new LruCache(cacheType.calculateCacheSize(application)) : mCacheFactory;
    }

    public static final class Builder {

        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private BaseImageLoaderStrategy loaderStrategy;
        private GlobalHttpHandler httpHandler;
        private ResponseErrorListener responseErrorListener;
        private List<Interceptor> interceptors;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkHttpConfiguration okHttpConfiguration;
        private AppModule.GsonConfiguration gsonConfiguration;
        private RequestInterceptor.Level printHttpLogLevel;
        private FormatPrinter formatPrinter;
        private Cache.Factory cacheFactory;

        private Builder() {
        }

        public Builder baseUrl(String baseUrl){
            if (TextUtils.isEmpty(baseUrl)){
                throw new IllegalArgumentException("BaseUrl cannot be null!");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseUrl(BaseUrl baseUrl){
            this.baseUrl = Preconditions.checkNotNull(baseUrl, BaseUrl.class.getCanonicalName() + "cannot be null!");
            return this;
        }

        //处理图片加载
        public Builder imageLoaderStrategy(BaseImageLoaderStrategy loaderStrategy){
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        //处理Http响应结果
        public Builder globalHttpHandler(GlobalHttpHandler httpHandler){
            this.httpHandler = httpHandler;
            return this;
        }

        public Builder responseErrorListener(ResponseErrorListener listener){
            this.responseErrorListener = listener;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor){
            if (interceptors == null){
                interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptor);
            return this;
        }

        public Builder cacheFile(File cacheFile){
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration configuration){
            this.retrofitConfiguration = configuration;
            return this;
        }

        public Builder okHttpConfiguration(ClientModule.OkHttpConfiguration configuration){
            this.okHttpConfiguration = configuration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration configuration){
            this.gsonConfiguration = configuration;
            return this;
        }

        public Builder printHttpLogLevel(RequestInterceptor.Level level){
            this.printHttpLogLevel = Preconditions.checkNotNull(printHttpLogLevel, "The printHttpLogLevel can not be null, use RequestInterceptor.Level.NONE instead.");;
            return this;
        }

        public Builder formatPrinter(FormatPrinter printer){
            this.formatPrinter = Preconditions.checkNotNull(formatPrinter, FormatPrinter.class.getCanonicalName() + "can not be null.");
            return this;
        }

        public Builder cacheFactory(Cache.Factory factory){
            this.cacheFactory = cacheFactory;
            return this;
        }

        public GlobalConfigModule build(){
            return new GlobalConfigModule(this);
        }
    }
}
