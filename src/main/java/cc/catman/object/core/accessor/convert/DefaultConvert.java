package cc.catman.object.core.accessor.convert;

import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import cc.catman.object.core.util.ReflectionHelper;

import java.util.Objects;
/**
 * 默认类型转换器
 * @author jpanda
 * @since 0.0.3
 */
public class DefaultConvert implements IConvert{

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Object o, Class<T> clazz) {
        if (Objects.isNull(o)){
            return null;
        }
        if (ReflectionHelper.isAssignableFrom(clazz,o.getClass())){
            return (T)o;
        }
        if (String.class.equals(clazz)){
            return (T)o.toString();
        }
        throw new PropertyAccessorRuntimeException("cannot be cast to "+clazz.getName());
    }
}
