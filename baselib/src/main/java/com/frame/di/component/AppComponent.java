package com.frame.di.component;

import android.app.Application;

import com.frame.base.delegate.AppDelegate;
import com.frame.base.delegate.AppDelegateImp;
import com.frame.di.module.AppModule;
import com.frame.di.module.ClientModule;
import com.frame.di.module.GlobalConfigModule;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.http.imageloader.ImageLoader;
import com.frame.integration.AppManager;
import com.frame.integration.IRepositoryManager;
import com.frame.integration.cache.Cache;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * ======================================
 * 可通过
 * <p>
 * Create by ChenJing on 2018-03-12 15:17
 * ======================================
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    Application application();

    //用于管理所有Activity
    AppManager appManager();

    //用于管理网络请求层以及数据缓存层
    IRepositoryManager repoitoryManager();

    RxErrorHandler rxErrorHandler();

    ApiHelper apiHelper();

    //图片加载管理
    ImageLoader imageLoader();

    OkHttpClient okhttpClient();

    Gson gson();

    //缓存文件根目录（Glide等的缓存已经作为子文件夹放在这个目录下），可将所有缓存都放在这个文件夹下面，便于管理和清理，可在GlobalConfigModule中配置
    File cacheFile();

    //用来存取一些整个App公用数据，不能大量存放大容量数据
    Cache<String, Object> extras();

    //用来创建缓存对象的工厂
    Cache.Factory cacheFactory();

    void inject(AppDelegateImp appDelegateImp);

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        Builder globalConfigModule(GlobalConfigModule globalConfigModule);
        AppComponent build();
    }
}
