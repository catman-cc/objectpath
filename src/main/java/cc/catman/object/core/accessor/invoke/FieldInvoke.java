package cc.catman.object.core.accessor.invoke;

import cc.catman.object.core.exception.ReflectionRuntimeException;

import java.lang.reflect.Field;

/**
 * 字段调用器
 * @author jpanda
 * @since 0.0.1
 */
public class FieldInvoke implements Invoke{
    private final Field field;

    public static FieldInvoke of(Field field) {
        return new FieldInvoke(field);
    }

    @SuppressWarnings("java:S3011")
    public FieldInvoke(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override
    @SuppressWarnings("java:S3011")
    public Object invoke(Object object, Object... args) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ReflectionRuntimeException(e);
        }
    }
}
