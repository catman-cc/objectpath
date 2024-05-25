package cc.catman.object.core.accessor;

import cc.catman.object.core.ConfigurationAccessor;
import cc.catman.object.core.Entity;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 指定对象访问器,有一些特定的对象不应该使用反射去访问字段
 * 比如,Date对象,应该被视为时间戳或者字符串
 *
 * @author jpanda
 * @since 0.0.1
 */
public interface ObjectAccessor extends ConfigurationAccessor {
    /**
     * 是否支持访问
     *
     * @param object 对象
     * @param key    属性
     * @return 是否支持
     */
    default boolean isSupport(Object object, Object key) {
        return isSupport(object, key, EAccessorKind.GET);
    }

    /**
     * 是否支持访问
     *
     * @param object 对象
     * @param kind   访问类型
     * @return 是否支持
     */
    default boolean isSupport(Object object, EAccessorKind kind) {
        return isSupport(object, null, kind);
    }

    /**
     * 是否支持访问
     *
     * @param object 对象
     * @param key    属性
     * @param kind   访问类型
     * @return 是否支持
     */
    boolean isSupport(Object object, Object key, EAccessorKind kind);

    /**
     * 获取对象的属性
     *
     * @param object 对象
     * @param key    属性
     * @return 属性值
     */
    Object get(Object object, Object key);

    /**
     * 获取对象的属性
     *
     * @param object   对象
     * @param consumer 消费者
     */
    default void eachValue(Object object, Consumer<Object> consumer) {
        throw new UnsupportedOperationException("不支持eachValue操作");
    }

    /**
     * 获取对象的属性
     *
     * @param object   对象
     * @param consumer 消费者
     */
    default void eachKey(Object object, Consumer<Object> consumer) {
        throw new UnsupportedOperationException("不支持eachKey操作");
    }

    /**
     * 获取对象的属性
     *
     * @param object   对象
     * @param consumer 消费者
     */
    default void eachEntry(Object object, Consumer<Entity> consumer) {
        throw new UnsupportedOperationException("不支持eachEntry操作");
    }

    /**
     * 获取对象的属性
     * @param object 对象
     * @param mapper 消费者
     * @return 映射后的对象
     */
    @SuppressWarnings("all")
    default Object map(Object object, Function<Object, Object> mapper) {
        throw new UnsupportedOperationException("不支持map操作");
    }

    /**
     * 获取对象的属性
     *
     * @param object 对象
     * @param filter 过滤器
     * @return 过滤后的对象
     */
    default Object filter(Object object, Predicate<Object> filter) {
        throw new UnsupportedOperationException("不支持filter操作");
    }

    /**
     * 获取对象的大小,如果对象不支持,则返回-1
     *
     * @param object 对象
     * @return 大小
     */
    default int size(Object object) {
        return -1;
    }

    /**
     * 将当前上下文对象转换为List
     *
     * @param object 对象
     * @return List
     */
    default List<Object> covertToList(Object object) {
        throw new UnsupportedOperationException("不支持转换为List");
    }

    /**
     * 将当前上下文对象转换为字符串
     *
     * @param object 对象
     * @return 字符串
     */
    default String covertToString(Object object) {
        throw new UnsupportedOperationException("不支持转换为字符串");
    }

    /**
     * 将对象转换为数值
     *
     * @param object 对象
     * @return 数值
     */
    default Number covertToNumber(Object object) {
        throw new UnsupportedOperationException("不支持转换为数值");
    }
}
