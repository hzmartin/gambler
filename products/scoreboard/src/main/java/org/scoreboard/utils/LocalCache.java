package org.scoreboard.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class LocalCache {
    private Map<ExpireTimeBasedTag, Cache<Object, Object>> map = Maps.newHashMap();

    private Cache<Object, Object> getCache(ExpireTimeBasedTag tag){
        Cache<Object, Object> cache = map.get(tag);
        if(cache == null){
            synchronized (LocalCache.this) {
                cache = map.get(tag);
                if(cache == null){
                    cache = CacheBuilder
                            .newBuilder().expireAfterWrite(tag.getExpireSeconds(), TimeUnit.SECONDS)
                            .maximumSize(tag.getMaxObjects()).recordStats()
                            .build();
                    map.put(tag, cache);
                }
            }
        }
        return cache;
    }

    @SuppressWarnings("unchecked")
    public <K,V> V get(ExpireTimeBasedTag tag, K key, Callable<V> loader) throws ExecutionException {
        return (V) getCache(tag).get(key, loader);
    }

    public <K,V> List<V> get(ExpireTimeBasedTag tag, Set<K> keys, Callable<V> loader) throws ExecutionException {
        List<V> accounts = Lists.newArrayList();
        for (K k: keys) {
            accounts.add(get(tag, k, loader));
        }
        return accounts;
    }

    public <K> void remove(ExpireTimeBasedTag tag, K key){
        getCache(tag).invalidate(key);
    }
}
