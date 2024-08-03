package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Map属性访问器
 * @author jpanda
 * @since 0.0.3
 */
public class MapPropertyAccessor extends AbstractPropertyAccessor {

    public MapPropertyAccessor(ObjectPathConfiguration configuration) {
        super(configuration);
    }

    public MapPropertyAccessor(ObjectPathConfiguration configuration, Object indexOrName) {
        super(configuration, indexOrName);
    }

    @Override
    public Object get(Object object) {
        if (this.isRoot()){
            return object;
        }
        if (Objects.isNull(object)){
            return null;
        }
        Map<Object, Object> map = tryCast2Map(object);
        return map.get(this.indexOrName);
    }

    @Override
    public Object get(PropertyWrapper object) {
        return this.get(object.read());
    }

    /**
     * 访问子属性/子元素
     *
     * @param object      对象
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @return 子属性或者子元素的属性包装器
     */
    @Override
    public PropertyWrapper get(Object object, Object indexOrName) {
        return this.get(propertyWrapperFactory.create(object),indexOrName);
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
        Map<Object, Object> map = tryCast2Map(object);
        Optional<Object> first = map.values().stream().findFirst();
        return first.map(Object::getClass).orElse(null);
    }

    @Override
    public PropertyAccessor createChildAccessor(PropertyWrapper belong, Object indexOrName) {
        return configuration.getPropertyAccessorFactory().create(belong,indexOrName);
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
        if (this.isRoot()){
            throw new PropertyAccessorRuntimeException("Can not set root property");
        }
        Map<Object, Object> map = tryCast2Map(object);

        map.put(this.indexOrName,value);
        return value;
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
        if (object.isNull()){
            object.init();
        }
        Object read=object.read();
        return this.set(read,indexOrName, value);
    }


    @SuppressWarnings("unchecked")
    protected Map<Object,Object> tryCast2Map(Object object){
        if (object instanceof Map){
            return (Map<Object,Object>) object;
        }
        // 如果不是Map类型,则抛出异常
        throw new PropertyAccessorRuntimeException("Object is not a Map");
    }
}
