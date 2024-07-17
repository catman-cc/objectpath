package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.util.ReflectionHelper;

import java.util.Map;
import java.util.Objects;

/**
 * Map属性访问器创建器
 * @author jpanda
 * @since 0.0.3
 */
public class MapPropertyAccessorCreator extends AbstractPropertyAccessorCreator{

    @Override
    public int score(Class<?> type, Object indexOrName) {
        if (Objects.isNull(type)){
            return -1;
        }
        if (ReflectionHelper.isAssignableFrom(Map.class,type)){
            return 5;
        }
        return 0;
    }

    /**
     * 创建属性访问器
     *
     * @param type           所属对象的类型
     * @param indexOrKeyName 属性名称或者索引
     * @return 属性访问器
     */
    @Override
    public PropertyAccessor create(Class<?> type, Object indexOrKeyName) {
        return new MapPropertyAccessor(configuration,indexOrKeyName);
    }

    @Override
    public PropertyAccessor create(PropertyWrapper belong, Object indexOrKeyName) {
        return new MapPropertyAccessor(configuration, indexOrKeyName);
    }
}