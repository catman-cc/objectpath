package cc.catman.object.core.accessor;

import cc.catman.object.core.exception.ReflectionRuntimeException;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 方法调用器
 * @author jpanda
 * @since 0.0.1
 */
public class MethodInvoke implements Invoke{

    @Getter
    private final Method method;

    public static MethodInvoke of(Method method) {
        return new MethodInvoke(method);
    }

    @SuppressWarnings("java:S3011")
    public MethodInvoke(Method method) {
        this.method = method;
        this.method.setAccessible(true);
    }

    @Override
    @SuppressWarnings("java:S3011")
    public Object invoke(Object object,Object... args) {
        try {
            return method.invoke(object,args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionRuntimeException(e);
        }
    }
}
