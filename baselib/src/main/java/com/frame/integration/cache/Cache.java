package com.frame.integration.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * ========================================
 * 缓存框架中必需的组件, 可通过....为框架提供缓存策略
 * 亦可用于其他数据的缓存
 * <p>
 * Create by ChenJing on 2018-03-13 15:16
 * ========================================
 */

public interface Cache<K, V> {

    interface Factory{

        /**
         * Create a new cache
         * @return
         */
        @NonNull
        Cache build(CacheType cacheType);
    }

    /**
     * 返回当前缓存占用大小
     * @return
     */
    int size();

    /**
     * 获取当前缓存允许的最大 Size
     * @return
     */
    int getMaxSize();

    /**
     * 返回 {@code key} 在缓存中对应的 {@code value}，若返回 null 则说明指定 key 没有对应的 value
     * @param key
     * @return
     */
    @Nullable
    V get(K key);

    /**
     * 将 {@code key} 和 {@code value} 以条目的形式加入缓存, 如果这个{@code key} 在缓存中有对应的 {@code value}
     * 则旧 {@code value} 被新{@code value} 替代并返回， 若返回 null 则说明这是一个新的条目
     * @param key
     * @param value
     * @return
     */
    @Nullable
    V put(K key, V value);

    /**
     * 移除缓存中对应 {@code key} 的条目， 并返回此条目的 value
     * @param key
     * @return
     */
    @Nullable
    V remove(K key);

    /**
     * 如果指定 {@code key} 在缓存中有对应的 value 并不为 null 则返回 true,否则返回 false
     * @param key
     * @return
     */
    boolean containsKey(K key);

    /**
     * 返回当前缓存中所有的{@code key}
     * @return
     */
    Set<K> keySet();

    /**
     * 清楚缓存中所有内容
     */
    void clear();
}
