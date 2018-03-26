package com.frame.integration;

import android.content.Context;

/**
 * ======================================
 * 用来管理网络请求层，以及数据缓存层，以及可能添加的数据库请求层
 * 提供给 {@link com.frame.mvp.IModel}
 *
 * Create by ChenJing on 2018-03-12 14:44
 * ======================================
 */

public interface IRepositoryManager {

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     * @param cache
     * @param <T>
     * @return
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    /**
     * 获取上下文
     * @return
     */
    Context getContext();
}