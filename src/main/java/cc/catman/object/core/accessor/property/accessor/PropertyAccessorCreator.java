package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.core.ConfigurationAccessor;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;

/**
 * 属性访问器 创建器
 * @author jpanda
 * @since 0.0.3
 */
public interface PropertyAccessorCreator extends ConfigurationAccessor {

    int score(Object object, Object indexOrName);


    int score(Class<?> type, Object indexOrName);

    int score(PropertyWrapper belong, Object indexOrName);
    /**
     * 创建属性访问器
     * @param type 所属对象的类型
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    PropertyAccessor create(Class<?> type, Object indexOrKeyName);

    /**
     * 创建属性访问器
     * @param belong  属性所属的对象
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    PropertyAccessor create(Object belong,Object indexOrKeyName);

    PropertyAccessor create(PropertyWrapper belong, Object indexOrKeyName);
}
