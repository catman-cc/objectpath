package cc.catman.object.cache;

import cc.catman.object.core.cache.ICache;
import cc.catman.object.core.util.CacheHelper;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 基于Caffeine的缓存
 *
 * @param <T> 被缓存的对象
 */
public class CaffeineICache<T> implements ICache<T> {

    private Cache<Object, Cache<Object, T>> cache;

    private Supplier<Cache<Object, T>> cacheCreator;

    public CaffeineICache(Cache<Object, Cache<Object, T>> cache, Supplier<Cache<Object, T>> cacheCreator) {
        this.cache = cache;
        this.cacheCreator = cacheCreator;
    }

    /**
     * 获取对象
     *
     * @param group 缓存时的分组,比如类名/特定的map集合/特定的对象
     * @param key   缓存的主键
     */
    @Override
    public Optional<T> get(Object group, Object key) {
        Cache<Object, T> sc = this.getOrSetGroupCache(group);
        Object subKey= CacheHelper.ensureNotNull(key);
        return Optional.ofNullable(sc.getIfPresent(subKey));
    }

    /**
     * 获取对象,如果缓存中不存在访问器,则创建并放入缓存中
     *
     * @param group   缓存时的分组
     * @param key     缓存的主键
     * @param creator 对象创建器
     * @return 对象
     */
    @Override
    public T getOrSet(Object group, Object key, Supplier<T> creator) {
        Cache<Object, T> sk = getOrSetGroupCache(group);
        Object subKey= CacheHelper.ensureNotNull(key);

        T v = sk.getIfPresent(subKey);
        if (Objects.nonNull(v)) {
            return v;
        }
        T value = creator.get();
        sk.put(subKey, value);
        return value;
    }

    /**
     * 将对象放入缓存中
     *
     * @param group    缓存分组
     * @param key      缓存主键
     * @param accessor 对象
     */
    @Override
    public void put(Object group, Object key, T accessor) {
        Object subKey= CacheHelper.ensureNotNull(key);
        Cache<Object, T> sc = this.getOrSetGroupCache(group);
        sc.put(subKey,accessor);
    }

    /**
     * 清除指定的缓存
     *
     * @param group 缓存分组
     * @param key   缓存主键
     */
    @Override
    public void invalidate(Object group, Object key) {
        Object subKey= CacheHelper.ensureNotNull(key);
        Cache<Object, T> subCache = this.getOrSetGroupCache(group);
        Optional.ofNullable(subCache).ifPresent(c -> c.invalidate(subKey));
    }

    /**
     * 清除指定分组的缓存
     *
     * @param group 缓存分组
     */
    @Override
    public void invalidateAll(Object group) {
        this.cache.invalidate(group);
    }

    /**
     * 清除所有缓存
     */
    @Override
    public void clear() {
        this.cache.cleanUp();
    }

    protected Cache<Object,T> getOrSetGroupCache(Object group){
        Cache<Object, T> sk = this.cache.getIfPresent(group);
        if (Objects.nonNull(sk)){
            return sk;
        }
        Cache<Object, T> sc = this.cacheCreator.get();
        this.cache.put(group,sc);
        return sc;
    }
}
