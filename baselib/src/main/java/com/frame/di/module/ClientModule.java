package com.frame.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.frame.http.GlobalHttpHandler;
import com.frame.http.api.ApiHelper;
import com.frame.http.log.RequestInterceptor;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ======================================
 * 提供一些三方库客户端实例 {@link Module}
 * <p>
 * Create by ChenJing on 2018-03-12 15:30
 * ======================================
 */
@Module
public abstract class ClientModule {

    private static final int TIME_OUT = 10;

    @Singleton
    @Provides
    static Retrofit provideRetrofit(Application application, @Nullable RetrofitConfiguration configuration, Retrofit.Builder builder, OkHttpClient client,
                                    HttpUrl httpUrl, Gson gson) {
        builder.baseUrl(httpUrl)//设置域名
                .client(client); //设置OkHttp
        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

    @Singleton
    @Provides
    static OkHttpClient provideClient(Application application, @Nullable OkHttpConfiguration configuration, OkHttpClient.Builder builder, Interceptor intercept,
                                      @Nullable List<Interceptor> interceptors, @Nullable GlobalHttpHandler handler) {
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);
        if (handler != null){
            builder.addInterceptor(chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())));
        }
        if (interceptors != null){
            for (Interceptor interceptor : interceptors){
                builder.addInterceptor(interceptor);
            }
        }
        if (configuration != null){
            configuration.configOkHttp(application, builder);
        }
        return builder.build();
    }

    @Singleton
    @Provides
    static Retrofit.Builder provideRetrofitBuilder(){
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    static OkHttpClient.Builder provideClientBuilder(){
        return new OkHttpClient.Builder();
    }

    @Binds
    abstract Interceptor bindInterceptor(RequestInterceptor interceptor);

    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    public interface OkHttpConfiguration {
        void configOkHttp(Context context, OkHttpClient.Builder builder);
    }
}