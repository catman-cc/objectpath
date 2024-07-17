package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.core.ConfigurationAccessor;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;

/**
 * 属性访问器工厂
 * @author jpanda
 * @since 0.0.3
 */
public interface PropertyAccessorFactory extends ConfigurationAccessor {
    /**
     * 创建属性访问器
     * @param type 所属对象的类型
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    PropertyAccessor create(Class<?> type,Object indexOrKeyName);

    /**
     * 创建属性访问器
     * @param belong  属性所属的对象
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    PropertyAccessor create(Object belong,Object indexOrKeyName);

    /**
     * 创建属性访问器
     * @param belong 属性所属的对象包装器
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    PropertyAccessor create(PropertyWrapper belong,Object indexOrKeyName);

    /**
     * 创建一个针对根对象的属性访问器,跟对象的属性访问器是不支持赋值操作的
     * @return 属性访问器
     */
    PropertyAccessor createRoot();
}
