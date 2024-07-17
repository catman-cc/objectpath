package cc.catman.object.core.accessor.invoke;

import cc.catman.object.core.util.ReflectionHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
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
        String keyString = parseKeyName(name);
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



    /**
     * 根据类和属性名查找属性
     * @param clazz 类
     * @param name 属性名
     * @param valueType 属性值类型
     * @return 属性访问器
     */
    public static Invoke findWriteInvoke(Class<?> clazz, String name, Class<?> valueType){
        String keyString = parseKeyName(name);
        Optional<Field> fieldOpt = tryGetFieldAccessor(clazz,keyString);
        if (fieldOpt.isPresent()) {
            Field field = fieldOpt.get();
            if (Objects.isNull(valueType)){
                return FieldInvoke.of(field);
            }
            if(ReflectionHelper.isAssignableFrom(field.getType(),valueType)){
                return FieldInvoke.of(field);
            }
        }
        // 如果找不到属性,则尝试寻找set方法
        return tryGetMethodAccessor(clazz,setMethodName(keyString))
                .filter(method -> {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        return parameterTypes.length == 1 && ReflectionHelper.isAssignableFrom(parameterTypes[0],valueType);
                })
                .map(MethodInvoke::of)
                .orElseThrow(()->new IllegalArgumentException("can not find write accessor for "+name));
    }

    /**
     * 解析关键字,主要是处理双引号和单引号
     * @param name 关键字
     * @return 关键字
     */
    private static String parseKeyName(String name) {
        String keyString = name;
        // 这里需要注意,keyString可能被包裹在了引号中,我们需要去掉引号
        if (keyString.startsWith("\"") && keyString.endsWith("\"")) {
            keyString = keyString.substring(1, keyString.length() - 1);
        }
        // 去除单引号
        if (keyString.startsWith("'") && keyString.endsWith("'")) {
            keyString = keyString.substring(1, keyString.length() - 1);
        }
        return keyString;
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

    private static String setMethodName(String key) {
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

}
