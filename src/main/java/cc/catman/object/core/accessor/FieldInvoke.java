package cc.catman.object.core.accessor;

import java.lang.reflect.Field;

/**
 * 字段调用器
 * @author jpanda
 * @since 0.0.1
 */
public class FieldInvoke implements Invoke{
    private Field field;

    public static FieldInvoke of(Field field) {
        return new FieldInvoke(field);
    }

    public FieldInvoke(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override
    public Object invoke(Object object, Object... args) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
