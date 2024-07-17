package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.util.CastHelper;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Collection集合属性访问器
 * @author jpanda
 * @since 0.0.3
 */
public class CollectionPropertyAccessor extends AbstractPropertyAccessor {

    public CollectionPropertyAccessor(ObjectPathConfiguration configuration) {
        super(configuration);
    }

    public CollectionPropertyAccessor(ObjectPathConfiguration configuration, Object indexOrName) {
        super(configuration, indexOrName);
    }

    /**
     * 获取属性值
     *
     * @param object 对象
     * @return 属性值
     */
    @Override
    public Object get(Object object) {
        if (this.isRoot()) {
            return object;
        }
        Collection<Object> collection = tryCast2Collection(object);
        if (collection == null) {
            return null;
        }
        if (this.indexOrName != null) {
            return collection.toArray()[tryCast2Integer(this.indexOrName)];
        }
        return null;
    }

    @Override
    public Class<?> getType(PropertyWrapper belong, Object indexOrName) {
        if (Objects.isNull(belong)) {
            return null;
        }
        Object object = belong.read();
        if (Objects.isNull(object)) {
            return null;
        }
        Collection<Object> collection = tryCast2Collection(object);
        Optional<Object> first = collection.stream().findFirst();
        return first.map(Object::getClass).orElse(null);
    }

    @Override
    public PropertyAccessor createChildAccessor(PropertyWrapper belong, Object indexOrName) {
        return new CollectionPropertyAccessor(this.configuration, indexOrName);
    }

    /**
     * 设置属性值
     *
     * @param object 对象
     * @param value  属性值
     * @return 设置后的属性值
     */
    @Override
    public Object set(Object object, Object value) {
        if (this.isRoot()) {
            throw new PropertyAccessorRuntimeException("Can not set root property");
        }
        throw new PropertyAccessorRuntimeException("use set-method,please use List Or Map");
    }

    /**
     * 设置子属性
     *
     * @param object      对象
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @param value       属性值
     * @return 设置后的属性值
     */
    @Override
    public Object set(PropertyWrapper object, Object indexOrName, Object value) {
        if (object.isNull()) {
            object.init();
        }
        Object read = object.read();
        return this.set(read, indexOrName, value);
    }

    @SuppressWarnings("unchecked")
    public Collection<Object> tryCast2Collection(Object object) {
        if (object instanceof Collection) {
            return (Collection<Object>) object;
        }
        // 如果不是Set类型,则抛出异常
        throw new PropertyAccessorRuntimeException("Object is not a Collection");
    }

    protected Integer tryCast2Integer(Object indexOrName) {
        return CastHelper.castToInt(indexOrName);
    }
}
