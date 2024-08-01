package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.invoke.*;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.exception.ReflectionRuntimeException;
import cc.catman.object.core.util.ReflectionHelper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * Field属性访问器
 *
 * @author jpanda
 * @since 0.0.3
 */
@Slf4j
public class ObjectPropertyAccessor extends AbstractPropertyAccessor implements PropertyAccessor {

    private Invoke invoke;

    private Invoke setInvoke;

    private InvokeFinder finder;

    public ObjectPropertyAccessor(ObjectPathConfiguration configuration) {
        super(configuration);
    }

    public ObjectPropertyAccessor(ObjectPathConfiguration configuration, Object indexOrName) {
        super(configuration, indexOrName);
        if (configuration.isUseCacheForReflect()
        ) {
            log.debug("Using cached reflect finder.");
            this.finder = new CachedInvokeFinder(new StandardInvokeFinder());
        }
    }


    /**
     * 获取属性值
     *
     * @param object 对象
     * @return 属性值
     */
    @Override
    public Object get(Object object) {
        if (this.isRoot()) {
            return object;
        }
        if (Objects.isNull(object)) {
            if (configuration.isStopParseWhenNull()) {
                return null;
            }
            return configuration.isUseZeroForNull() ? 0 : null;
        }
        if (!Optional.ofNullable(this.invoke).isPresent()) {
            try {
                this.invoke = finder.find(object.getClass(), indexOrName.toString());
            }catch (ReflectionRuntimeException e){
                if (configuration.isAllowAccessFieldWhenNotFound()){
                    this.invoke=new NullInvoke();
                }else{
                    throw e;
                }
            }
        }
        if (Objects.isNull(this.invoke)){
            return null;
        }
        return this.invoke.invoke(object);
    }

    @Override
    public Object get(PropertyWrapper belong) {
        Object bo = belong.read();
        if (Objects.isNull(bo)){
            return null;
        }
        // 然后尝试获取属性值
        return this.get(bo);
    }

    @Override
    public Class<?> getType(PropertyWrapper belong, Object indexOrName) {
        Class<?> belongType = belong.readType();
        if (Objects.isNull(belongType)){
            return null;
        }
        // 基于反射获取类型
        String index=indexOrName.toString();
        Field field;
        try {
            field = ReflectionHelper.findField(belongType, index);
        } catch (PropertyAccessorRuntimeException e) {
            return null;
        }
        return field.getType();
    }

    @Override
    public PropertyAccessor createChildAccessor(PropertyWrapper belong, Object indexOrName) {
        return propertyAccessorFactory.create(belong.readType(),indexOrName);
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
        if(Objects.isNull(this.setInvoke)){
            this.setInvoke = this.finder.findWrite(object.getClass(), indexOrName.toString(),Objects.isNull(value)?null:value.getClass());
        }

        return this.setInvoke.invoke(object,value);
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
        return this.set(read,indexOrName, value);
    }

}
