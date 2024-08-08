package cc.catman.object.core.cache;


import java.util.Optional;
import java.util.function.Supplier;
/**
 * 缓存接口
 * @author jpanda
 * @since 0.0.3
 */
public interface ICache<T> {
    /**
     * 获取对象
     * @param group 缓存时的分组,比如类名/特定的map集合/特定的对象
     * @param key  缓存的主键
     * @return 可能存在的缓存值
     */
    Optional<T> get(Object group, Object key);

    /**
     * 获取对象,如果缓存中不存在访问器,则创建并放入缓存中
     * @param group 缓存时的分组
     * @param key 缓存的主键
     * @param creator 对象创建器
     * @return 对象
     */
    T getOrSet(Object group, Object key, Supplier<T> creator);

    /**
     * 将对象放入缓存中
     * @param group 缓存分组
     * @param key 缓存主键
     * @param accessor 对象
     */
    void put(Object group,Object key,T accessor);

    /**
     * 清除指定的缓存
     * @param group 缓存分组
     * @param key 缓存主键
     */
    void invalidate(Object group,Object key);

    /**
     * 清除指定分组的缓存
     * @param group 缓存分组
     */
    void invalidateAll(Object group);

    /**
     * 清除所有缓存
     */
    void clear();
}
