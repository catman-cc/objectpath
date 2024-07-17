package cc.catman.object.core.accessor.property;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 属性访问器缓存
 * @author jpanda
 * @since 0.0.3
 */
public interface PropertyAccessorCache {
    /**
     * 获取属性访问器
     * @param group 缓存时的分组,比如类名/特定的map集合/特定的对象
     * @param key  缓存的主键
     */
    Optional<PropertyAccessor> get(Object group, Object key);

    /**
     * 获取属性访问器,如果缓存中不存在访问器,则创建并放入缓存中
     * @param group 缓存时的分组
     * @param key 缓存的主键
     * @param creator 属性访问器创建器
     * @return 属性访问器
     */
    PropertyAccessor getOrSet(Object group, Object key, Supplier<PropertyAccessor> creator);

    /**
     * 将属性访问器放入缓存中
     * @param group 缓存分组
     * @param key 缓存主键
     * @param accessor 属性访问器
     */
    void put(Object group,Object key,PropertyAccessor accessor);

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
