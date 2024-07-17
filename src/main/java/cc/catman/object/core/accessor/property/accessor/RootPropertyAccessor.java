package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;

/**
 * 跟属性访问器
 * @author jpanda
 * @since 0.0.3
 */
public class RootPropertyAccessor extends AbstractPropertyAccessor{

    private PropertyAccessor realAccessor;

    public RootPropertyAccessor(ObjectPathConfiguration configuration) {
        super(configuration);
    }

    /**
     * 获取属性值
     *
     * @param object 对象
     * @return 属性值
     */
    @Override
    public Object get(Object object) {
        return object;
    }


    /**
     * 访问子属性/子元素
     *
     * @param belong      所属的属性包装器
     * @param indexOrName 子属性或者子元素的名称或者索引
     * @return 子属性或者子元素的属性包装器
     */
    @SuppressWarnings("unchecked")
    @Override
    public PropertyWrapper get(PropertyWrapper belong, Object indexOrName) {
        // 然后获取子属性的包装器
        return belong.read(indexOrName);
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
        throw new PropertyAccessorRuntimeException("Cannot set value to root property");
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
        throw new PropertyAccessorRuntimeException("Cannot set value to root property");
    }

    @Override
    public PropertyAccessor createChildAccessor(PropertyWrapper belong, Object indexOrName) {
        return this.getOrCreateRealAccessor().createChildAccessor(belong, indexOrName);
    }

    protected PropertyAccessor getOrCreateRealAccessor(){
        return realAccessor;
    }
}
