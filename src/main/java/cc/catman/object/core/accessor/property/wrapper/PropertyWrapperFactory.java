package cc.catman.object.core.accessor.property.wrapper;

import cc.catman.object.core.ConfigurationAccessor;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;

/**
 * 属性包装器创建工厂
 * @author jpanda
 * @since 0.0.3
 */
public interface PropertyWrapperFactory extends ConfigurationAccessor {
    /**
     * 创建属性包装器
     * 
     * @param object 被包装的对象
     * @return 属性包装器
     */
     PropertyWrapper create(Object object);

     PropertyWrapper createReadOnly(Object object);

    /**
     * 创建属性包装器
     * @param belong  该属性包装器所属的属性包装器
     * @param key 该属性包装器对应的key
     * @return 属性包装器
     */
    PropertyWrapper create(PropertyWrapper belong, Object key);

    /**
     * 创建属性包装器
     * 
     * @param belong 所属对象
     * @param key    属性名称或索引
     * @param propertyAccessor 属性访问器
     * @return 属性包装器
     */
    PropertyWrapper create(PropertyWrapper belong, Object key, PropertyAccessor propertyAccessor);

    PropertyWrapper create(Object belong,Object key);

}
