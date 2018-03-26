package com.frame.integration.cache;

import android.support.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ========================================
 * LRU: least recently used， 最近最少使用
 * 即当缓存满了，会有限淘汰那些最近最不常访问的数据
 * <p>
 * Create by ChenJing on 2018-03-13 15:58
 * ========================================
 */

public class LruCache<K, V> implements Cache<K, V>{

    private final LinkedHashMap<K, V> cache = new LinkedHashMap<>(100, 0.75f, true);
    private final int initialMaxSize;
    private int maxSize;
    private int currentSize = 0;

    /**
     * Constructor
     * @param size 这个缓存的最大值，使用的单位必须
     */
    public LruCache(int size) {
        this.initialMaxSize = size;
        this.maxSize = size;
    }

    /**
     * 设置一个系数应用于当时构造函数中所传入的size, 从而得到一个新的{@code maxSize}
     *
     * @param multiplier
     */
    protected synchronized void setSizeMultiplier(float multiplier){
        if (multiplier < 0){
            throw new IllegalArgumentException("Multiplier must be >=0.");
        }
        maxSize = Math.round(initialMaxSize * multiplier);
        evict();
    }

    /**
     * 返回每个 item 所占的 size, 默认为1， 单位必须跟构造函数传入的size一致
     * 子类可重写此方法以适应不同单位
     * @param item
     * @return
     */
    protected int getItemSize(V item){
        return 1;
    }

    /**
     * 当缓存中有条目被驱逐时，会回掉此方法
     * @param key
     * @param value
     */
    protected void onItemEvicted(K key, V value){
        // optional override
    }

    /**
     * 当指定的 size 小于当前缓存占用的总 size 时， 会开始清除缓存中最近最少使用的条目
     * @param size
     */
    protected synchronized void trimToSize(int size){
        Map.Entry<K, V> last;
        while (currentSize > size){
            last = cache.entrySet().iterator().next();
            final V toRemove = last.getValue();
            currentSize -= getItemSize(toRemove);
            final K key = last.getKey();
            cache.remove(key);
            onItemEvicted(key, toRemove);
        }
    }

    /**
     * 当缓存中已占用的总 size 大于允许的最大 size ， 会使用{@link #trimToSize(int)}开始清除满足条件的条目
     */
    private void evict(){
        trimToSize(maxSize);
    }

    @Override
    public synchronized int size() {
        return currentSize;
    }

    @Override
    public synchronized int getMaxSize() {
        return maxSize;
    }

    @Nullable
    @Override
    public synchronized V get(K key) {
        return cache.get(key);
    }

    @Nullable
    @Override
    public synchronized V put(K key, V value) {
        final int itemSize = getItemSize(value);
        if (itemSize >= maxSize){
            onItemEvicted(key, value);
            return null;
        }
        final V result = cache.put(key, value);
        if (value != null){
            currentSize += getItemSize(value);
        }
        if (result != null){
            currentSize -= getItemSize(result);
        }
        evict();
        return result;
    }

    @Nullable
    @Override
    public synchronized V remove(K key) {
        final V value = cache.remove(key);
        if (value != null){
            currentSize -= getItemSize(value);
        }
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    @Override
    public synchronized Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public synchronized void clear() {
        trimToSize(0);
    }
}
