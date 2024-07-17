package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.util.ArrayHelper;
import cc.catman.object.core.util.CastHelper;

import java.util.Objects;


/**
 * 数组类型的属性访问器
 * @author jpanda
 * @since 0.0.3
 */
public class ArrayPropertyAccessor extends AbstractPropertyAccessor{
    public ArrayPropertyAccessor(ObjectPathConfiguration configuration) {
        super(configuration);
    }

    public ArrayPropertyAccessor(ObjectPathConfiguration configuration, Object indexOrName) {
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
        if (this.isRoot()){
            return object;
        }
        return ArrayHelper.get(object, CastHelper.castToInt(this.indexOrName));
    }

    @Override
    public Class<?> getType(PropertyWrapper belong, Object indexOrName) {
        if (Objects.isNull(belong)){
            return null;
        }
        Object object = belong.read();
        if (Objects.isNull(object)){
            return null;
        }
        Object o = ArrayHelper.get(object, 0);
        if (Objects.isNull(o)){
            return null;
        }
        return o.getClass();
    }

    @Override
    public PropertyAccessor createChildAccessor(PropertyWrapper belong, Object indexOrName) {
        return new ArrayPropertyAccessor(configuration,indexOrName);
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
        if (object == null){
           throw new PropertyAccessorRuntimeException("can not set value for null");
        }
        if ( ArrayHelper.set(object, CastHelper.castToInt(this.indexOrName),value)){
            return value;
        }
        throw new PropertyAccessorRuntimeException("index out bounds for value, target:" + ArrayHelper.toString(object,",") + ",index:" + this.indexOrName);
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
        return this.set(object,indexOrName,value);
    }

}
