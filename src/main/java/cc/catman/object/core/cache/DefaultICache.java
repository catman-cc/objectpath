package cc.catman.object.core.cache;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Supplier;
/**
 * 缓存接口
 * @author jpanda
 * @since 0.0.3
 */
public class DefaultICache<T> implements ICache<T> {
    /**
     * 缓存分组
     */
    protected Map<Object, Map<Object, T>> groupedCache=new WeakHashMap<>();

    /**
     * 获取缓存对象
     *
     * @param group 缓存时的分组,比如类名/特定的map集合/特定的对象
     * @param key   缓存的主键
     */
    @Override
    public Optional<T> get(Object group, Object key) {
        return  Optional.ofNullable(groupedCache.get(group)).map(g->g.get(key));
    }

    /**
     * 获取缓存对象,如果缓存中不存在访问器,则创建并放入缓存中
     *
     * @param group   缓存时的分组
     * @param key     缓存的主键
     * @param creator 缓存对象创建器
     * @return 缓存对象
     */
    @Override
    public T getOrSet(Object group, Object key, Supplier<T> creator) {
        Map<Object, T> g = groupedCache.computeIfAbsent(group, k -> new WeakHashMap<>());
        return g.computeIfAbsent(key,k->creator.get());
    }

    /**
     * 将缓存对象放入缓存中
     *
     * @param group    缓存分组
     * @param key      缓存主键
     * @param accessor 缓存对象
     */
    @Override
    public void put(Object group, Object key, T accessor) {
        Map<Object, T> g = groupedCache.computeIfAbsent(group, k -> new WeakHashMap<>());
        g.put(key, accessor);
    }

    /**
     * 清除指定的缓存
     *
     * @param group 缓存分组
     * @param key   缓存主键
     */
    @Override
    public void invalidate(Object group, Object key) {
        Optional.ofNullable(this.groupedCache.get(group)).ifPresent(sc-> sc.remove(key));
    }

    /**
     * 清除指定分组的缓存
     *
     * @param group 缓存分组
     */
    @Override
    public void invalidateAll(Object group) {
        this.groupedCache.remove(group);
    }

    /**
     * ]清除所有缓存
     */
    @Override
    public void clear() {
        this.groupedCache.clear();
    }
}
