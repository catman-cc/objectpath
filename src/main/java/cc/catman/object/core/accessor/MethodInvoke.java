package cc.catman.object.core.accessor;

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

    public MethodInvoke(Method method) {
        this.method = method;
        this.method.setAccessible(true);
    }

    @Override
    public Object invoke(Object object,Object... args) {
        try {
            return method.invoke(object,args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
