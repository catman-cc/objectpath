package cc.catman.object.core.accessor.property;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * 默认属性访问器缓存,使用弱引用缓存
 * @author jpanda
 * @since 0.0.3
 */
public class DefaultPropertyAccessorCache implements PropertyAccessorCache{
    /**
     * 缓存分组
     */
    protected Map<Object, Map<Object,PropertyAccessor>> groupedCache=new WeakHashMap<>();

    /**
     * 获取属性访问器
     *
     * @param group 缓存时的分组,比如类名/特定的map集合/特定的对象
     * @param key   缓存的主键
     */
    @Override
    public Optional<PropertyAccessor> get(Object group, Object key) {
        return  Optional.ofNullable(groupedCache.get(group)).map(g->g.get(key));
    }

    /**
     * 获取属性访问器,如果缓存中不存在访问器,则创建并放入缓存中
     *
     * @param group   缓存时的分组
     * @param key     缓存的主键
     * @param creator 属性访问器创建器
     * @return 属性访问器
     */
    @Override
    public PropertyAccessor getOrSet(Object group, Object key, Supplier<PropertyAccessor> creator) {
        Map<Object, PropertyAccessor> g = groupedCache.computeIfAbsent(group, k -> new WeakHashMap<>());
        return g.computeIfAbsent(key,k->creator.get());
    }

    /**
     * 将属性访问器放入缓存中
     *
     * @param group    缓存分组
     * @param key      缓存主键
     * @param accessor 属性访问器
     */
    @Override
    public void put(Object group, Object key, PropertyAccessor accessor) {
        Map<Object, PropertyAccessor> g = groupedCache.computeIfAbsent(group, k -> new WeakHashMap<>());
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
