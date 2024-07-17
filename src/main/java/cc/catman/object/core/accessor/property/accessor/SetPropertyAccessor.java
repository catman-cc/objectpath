package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.util.CastHelper;

import java.util.Objects;
import java.util.Set;

/**
 * Set属性访问器,用于访问Set类型属性
 * 但是,由于Set类型属性没有具体的属性值,因此,此处的get和set方法均返回null?抛出异常?
 * @author jpanda
 * @since 0.0.3
 */
public class SetPropertyAccessor extends AbstractPropertyAccessor {

    public SetPropertyAccessor(ObjectPathConfiguration configuration) {
        super(configuration);
    }

    public SetPropertyAccessor(ObjectPathConfiguration configuration, Object indexOrName) {
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
        Set<Object> set = tryCast2Set(object);
        Integer index = tryCast2Integer(this.indexOrName);
        if (index != null){
            if (index >= set.size()){
                if (this.configuration.isSkipOutOfRangeIndexForList()){
                    return null;
                }
                throw new PropertyAccessorRuntimeException("Index out of range");
            }
            return set.toArray()[index];
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
        Set<Object> set = tryCast2Set(object);
        return set.stream().findFirst().map(Object::getClass).orElse(null);
    }

    @Override
    public PropertyAccessor createChildAccessor(PropertyWrapper belong, Object indexOrName) {
        return new SetPropertyAccessor(configuration,indexOrName);
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
        // set集合的更新操作是有风险的,因为set的特性,新值的位置九成会发生变化
        if (!configuration.isAllowWriteForSet()){
            throw new PropertyAccessorRuntimeException("Can not set property for Set," +
                                                       "You can enable this feature with the " +
                                                       "allowWriteForSet configuration.");
        }
        Set<Object> set = tryCast2Set(object);
        if (set != null){
            // set的赋值,本身是移除旧值,再添加新值
            // 读取旧对象
            Object old = this.get(object);
            set.remove(old);
            set.add(value);
            // 重新获取新对象所处的位置,并修改当前的索引
            int index=0;
            for (Object o : set) {
                if (Objects.equals(o, value)){
                    this.indexOrName=index;
                    return value;
                }
                index++;
            }
            return value;
        }
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
        Object read = object.read();
        return this.set(read,indexOrName,value);
    }

    @SuppressWarnings("unchecked")
    public Set<Object> tryCast2Set(Object object){
        if (object instanceof Set){
            return (Set<Object>) object;
        }
        // 如果不是Set类型,则抛出异常
        throw new PropertyAccessorRuntimeException("Object is not a Set");
    }

    protected Integer tryCast2Integer(Object indexOrName){
        return CastHelper.castToInt(indexOrName);
    }
}
