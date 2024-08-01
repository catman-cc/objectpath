package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.util.CastHelper;

import java.util.List;
import java.util.Objects;

/**
 * 列表属性访问器
 * @author jpanda
 * @since 0.0.3
 */
public class ListPropertyAccessor extends AbstractPropertyAccessor {

    public ListPropertyAccessor(ObjectPathConfiguration configuration) {
        super(configuration);
    }

    public ListPropertyAccessor(ObjectPathConfiguration configuration, Object index) {
        super(configuration,index);
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
        if (Objects.isNull(object)){
            return null;
        }
        List<?> list=tryCast2List(object);
        try {
            return list.get(tryCast2Integer(indexOrName));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Object get(PropertyWrapper belong) {
       return this.get(belong.read());
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
        // 如果需要继续向下访问,首先需要获取子属性的访问器,然后通过子属性的访问器来构建子对象的访问器
        return this.get(propertyWrapperFactory.create(object),indexOrName);
    }

    @Override
    public Class<?> getType(PropertyWrapper belong, Object indexOrName) {
        Object obj = belong.read();
        if (Objects.isNull(obj)){
            return null;
        }
        List<Object> list=tryCast2List(obj);
        if (list.isEmpty()){
            return null;
        }
        Object o = list.get(0);
        return o.getClass();
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
            throw new PropertyAccessorRuntimeException("Cannot set value to root property");
        }
        List<Object> list=tryCast2List(object);
        Integer index=tryCast2Integer(this.indexOrName);
        if (index >= list.size()){
            // 如果索引越界,则扩容
            for (int i = list.size(); i <= index; i++) {
                list.add(null);
            }
        }
        list.set(index,value);
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
        if (object.isNull()) {
            object.init();
        }
        Object readObject = object.read();
        return this.set(readObject,indexOrName,value);
    }

    @SuppressWarnings("unchecked")
    protected List<Object> tryCast2List(Object object){
        if (object instanceof List){
            return (List<Object>) object;
        }
        // 如果不是List类型,则抛出异常
        throw new PropertyAccessorRuntimeException("Object is not a List");
    }

    protected Integer tryCast2Integer(Object indexOrName){
       return CastHelper.castToInt(indexOrName);
    }
}
