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
        if (ReflectionHelper.isSameTypeIgnorePrimaryType(Integer.class,clazz)){
            return (T)Integer.valueOf(tryCastToNumber(o).intValue());
        }
        if (ReflectionHelper.isAssignableFrom(Number.class,clazz)){
            return (T)tryCastToNumber(o);
        }
        if (ReflectionHelper.isAssignableFrom(Boolean.class,clazz)){
            return (T)tryCastToBoolean(o);
        }
        throw new PropertyAccessorRuntimeException("cannot be cast to "+clazz.getName());
    }


    protected Boolean tryCastToBoolean(Object o){
        Class<?> ot = o.getClass();
        if (ReflectionHelper.isAssignableFrom(Boolean.class, ot)){
            return (Boolean)o;
        }
        if (ReflectionHelper.isAssignableFrom(Number.class,ot)){
            return ((Number)o).doubleValue()==1;
        }
        if (ReflectionHelper.isAssignableFrom(String.class,ot)){
            String s = (String)o;
            return "TRUE".equalsIgnoreCase(s)
                    ||"T".equalsIgnoreCase(s);
        }
        return false;
    }

    protected Number tryCastToNumber(Object o){
        Class<?> ot = o.getClass();
        if (ReflectionHelper.isAssignableFrom(Number.class, ot)){
            return (Number)o;
        }
        if (ReflectionHelper.isAssignableFrom(String.class,ot)){
            String s = (String)o;
            return Double.parseDouble(s);
        }
        throw new PropertyAccessorRuntimeException("cannot be cast to Number: "+o.getClass().getName());
    }
}
