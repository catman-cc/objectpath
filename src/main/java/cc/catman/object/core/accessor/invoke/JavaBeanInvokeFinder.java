package cc.catman.object.core.accessor.invoke;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 根据javabean规范查找方法和字段的工具类
 * @author jpanda
 * @since 0.0.1
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JavaBeanInvokeFinder {


    /**
     * 根据类和方法名查找方法
     * 尝试寻找key对应的属性值,根据javabean的规范,我们将按照一下规则去寻找属性:
     * 1. get+key,如果存在该方法,则调用该方法
     * 2. is+key,如果存在该方法,则调用该方法
     * 3. key,如果存在该属性,则直接返回该属性
     * 4. 直接寻找对应key值的方法,如果存在,则调用该方法,但是不推荐这种方式
     * 5. 如果都不存在,则返回null
     *
     * @param clazz 类
     * @param name 方法名
     * @return 方法调用器
     */
    public static Invoke findInvoke(Class<?> clazz, String name) {
        String keyString = name;
        // 这里需要注意,keyString可能被包裹在了引号中,我们需要去掉引号
        if (keyString.startsWith("\"") && keyString.endsWith("\"")) {
            keyString = keyString.substring(1, keyString.length() - 1);
        }
        // 去除单引号
        if (keyString.startsWith("'") && keyString.endsWith("'")) {
            keyString = keyString.substring(1, keyString.length() - 1);
        }
        Optional<Method> methodOpt = tryGetMethodAccessor(clazz, keyString);
        if (methodOpt.isPresent()) {
            return MethodInvoke.of(methodOpt.get());
        }
        Optional<Field> fieldOpt = tryGetFieldAccessor(clazz,keyString);
        if (fieldOpt.isPresent()) {
            return FieldInvoke.of(fieldOpt.get());
        }

        Optional<Method> simpleMethodOpt = getMethod(clazz, keyString);
        return simpleMethodOpt.map(MethodInvoke::of).orElse(null);
    }

    private static Optional<Method> tryGetMethodAccessor(Class<?> clazz, String name){
        Method method=getMethod(clazz,getMethodName(name))
                .orElseGet(()->getMethod(clazz,isMethodName(name)).orElse(null));

        if(method!=null){
            return Optional.of(method);
        }
        return Optional.empty();
    }

    @SuppressWarnings("java:S3011")
    private static Optional<Field> tryGetFieldAccessor(Class<?> clazz,String name){
        Field field;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return Optional.empty();
        }
        field.setAccessible(true);
        return Optional.of(field);
    }
    @SuppressWarnings("java:S3011")
    private static Optional<Method> getMethod(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return Optional.of(method);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private static String getMethodName(String key) {
        return "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    private static String isMethodName(String key) {
        return "is" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }
}
