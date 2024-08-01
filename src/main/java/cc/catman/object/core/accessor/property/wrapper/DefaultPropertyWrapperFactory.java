package cc.catman.object.core.accessor.property.wrapper;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.util.ReflectionHelper;

import java.util.Objects;


/**
 * 默认的属性包装器创建工厂
 * @author jpanda
 * @since 0.0.3
 */
public class DefaultPropertyWrapperFactory implements PropertyWrapperFactory{

    protected ObjectPathConfiguration configuration;

    /**
     * 创建属性包装器
     *
     * @param object 被包装的对象
     * @return 属性包装器
     */
    @Override
    public PropertyWrapper create(Object object) {
        if (Objects.nonNull(object) && ReflectionHelper.isAssignableFrom(PropertyWrapper.class, object.getClass())) {
                return (PropertyWrapper) object;
            }

        return new DefaultPropertyWrapper<>(configuration,object);
    }

    @Override
    public PropertyWrapper createReadOnly(Object object) {
        if (Objects.nonNull(object) && ReflectionHelper.isAssignableFrom(PropertyWrapper.class, object.getClass())) {
            return (PropertyWrapper) object;
        }

        return new ReadOnlyPropertyWrapper<>(configuration,object);
    }

    /**
     * 创建属性访问器
     * @param belong  该属性包装器所属的属性包装器
     * @param key 该属性包装器对应的key
     * @return 属性包装器
     */
    @Override
    public PropertyWrapper create(PropertyWrapper belong, Object key) {
        if (belong.readOnly()){
            return new ReadOnlyPropertyWrapper<>(configuration,belong,key);
        }
        return new DefaultPropertyWrapper<>(configuration, belong, key);
    }

    /**
     * 创建属性包装器
     *
     * @param belong           所属对象
     * @param key              属性名称或索引
     * @param propertyAccessor 属性访问器
     * @return 属性包装器
     */
    @Override
    public PropertyWrapper create(PropertyWrapper belong, Object key, PropertyAccessor propertyAccessor) {
        if (belong.readOnly()){
            return new ReadOnlyPropertyWrapper<>(configuration,belong,key,propertyAccessor);
        }
        return new DefaultPropertyWrapper<>(configuration,belong,key,propertyAccessor);
    }

    @Override
    public PropertyWrapper create(Object belong, Object key) {
        return new DefaultPropertyWrapper<>(configuration,this.create(belong),key);
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration=configuration;
    }
}
