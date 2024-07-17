package cc.catman.object.core.accessor.property;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 一个空实现的属性访问器缓存,会丢弃所有缓存操作
 * @author jpanda
 * @since 0.0.3
 */
@Slf4j
public class DiscardPropertyAccessorCache implements PropertyAccessorCache {
    /**
     * 获取属性访问器
     *
     * @param group 缓存时的分组,比如类名/特定的map集合/特定的对象
     * @param key   缓存的主键
     */
    @Override
    public Optional<PropertyAccessor> get(Object group, Object key) {
        return Optional.empty();
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
        return creator.get();
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
        log.trace("Discard property accessor cache, group:{}, key:{}, accessor:{}", group, key, accessor);
    }

    /**
     * 清除指定的缓存
     *
     * @param group 缓存分组
     * @param key   缓存主键
     */
    @Override
    public void invalidate(Object group, Object key) {
        // do nothing
    }

    /**
     * 清除指定分组的缓存
     *
     * @param group 缓存分组
     */
    @Override
    public void invalidateAll(Object group) {
        // do nothing
    }

    /**
     * ]清除所有缓存
     */
    @Override
    public void clear() {
        // do nothing
    }
}
