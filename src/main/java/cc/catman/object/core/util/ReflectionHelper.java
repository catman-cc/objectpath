package cc.catman.object.core.util;

import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 反射帮助类
 *
 * @author jpanda
 * @since 0.0.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionHelper {

    /**
     * 基础类型映射表
     */
    private static final Map<Class<?>, Class<?>> PRIMARY_TYPES = new HashMap<>();

    /**
     * 反转基础类型映射表
     */
    private static final Map<Class<?>, Class<?>> REVERT_PRIMARY_TYPES = new HashMap<>();

    static {
        PRIMARY_TYPES.put(boolean.class, Boolean.class);
        PRIMARY_TYPES.put(byte.class, Byte.class);
        PRIMARY_TYPES.put(short.class, Short.class);
        PRIMARY_TYPES.put(int.class, Integer.class);
        PRIMARY_TYPES.put(long.class, Long.class);
        PRIMARY_TYPES.put(float.class, Float.class);
        PRIMARY_TYPES.put(double.class, Double.class);
        PRIMARY_TYPES.put(char.class, Character.class);
        PRIMARY_TYPES.put(void.class, Void.class);

        // 反转类型映射
        PRIMARY_TYPES.forEach((k, v) -> REVERT_PRIMARY_TYPES.put(v, k));
    }

    /**
     * 查找注解
     *
     * @param <T>             注解类型
     * @param element         元素
     * @param annotationClass 注解类型
     * @return 注解
     */
    public static <T extends Annotation> Optional<T> findAnnotation(AnnotatedElement element,
                                                                    Class<T> annotationClass) {
        return Optional.ofNullable(element.getDeclaredAnnotation(annotationClass));
    }

    /**
     * 判断是否是可赋值类型,额外支持基本类型
     *
     * @param targetType 目标类型
     * @param sourceType 源类型
     * @return 是否是可赋值类型
     */
    public static boolean isAssignableFrom(Class<?> targetType, Class<?> sourceType) {
        if (Objects.isNull(targetType)
            || Objects.isNull(sourceType)
        ) {
            return false;
        }
        if (isSameTypeIgnorePrimaryType(targetType, sourceType)) {
            return true;
        }
        return targetType.isAssignableFrom(sourceType);
    }

    /**
     * 获取原始类型
     *
     * @param type 类型
     * @return 原始类型
     */
    public static Class<?> getPrimaryType(Class<?> type) {
        Class<?> optClass = REVERT_PRIMARY_TYPES.get(type);
        if (Objects.nonNull(optClass)) {
            return optClass;
        }
        return type;
    }

    /**
     * 判断是否是同类型,忽略基本类型
     *
     * @param type1 类型1
     * @param type2 类型2
     * @return 是否是同类型
     */
    public static boolean isSameTypeIgnorePrimaryType(Class<?> type1, Class<?> type2) {
        return getPrimaryType(type1).equals(getPrimaryType(type2));
    }

    @SuppressWarnings("java:S3011")
    public static void makeAccessible(AccessibleObject e) {
        if (Objects.isNull(e)){
            return;
        }
        if (e.isAccessible()) {
            return;
        }
        e.setAccessible(true);
    }

    /**
     * 尝试查找字段
     *
     * @param type      类型
     * @param fieldName 字段名称
     * @return 字段
     */
    public static Field findField(Class<?> type, String fieldName) {
        fieldName = StringHelper.unquote(fieldName);
        if (Objects.nonNull(fieldName)) {
            try {
                Field f = type.getDeclaredField(fieldName);
                makeAccessible(f);
                return f;
            } catch (NoSuchFieldException ignored) {
                // ignored
            }
        }
        throw new PropertyAccessorRuntimeException("can not find field:" + fieldName + "with:" + type.getName());
    }

    public static Object invokeMethod(Object o, String methodName, List<Object> args,Predicate<Method> filter) {
        Method method = findMethod(o, methodName, args,filter);
        makeAccessible(method);
        assert method!=null;
        try {
            return method.invoke(o, args.toArray());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new PropertyAccessorRuntimeException("can not invoke method",e);
        }
    }

    public static Object invokeMethod(Object o, String methodName, List<Object> args) {
        return invokeMethod(o,methodName,args,m->true);
    }

    public static Method findMethod(Object o, String methodName, List<Object> args ){
        return findMethod(o,methodName,args,m->true);
    }

    @SuppressWarnings("java:S3776")
    public static Method findMethod(Object o, String methodName, List<Object> args, Predicate<Method> filter) {
        //  尝试获取指定名称的方法,这里需要注意的是,此处无法直接通过方法名来获取方法,因为方法名不唯一,
        //  所以还要对比方法参数列表来获取唯一的方法,考虑到实际参数列表可能包含null值,以及子类定义,所以需要遍历所有方法
        //  然后尝试获取到最匹配的方法,如果没有找到,则抛出异常
        //  最佳方法: 先获取所有方法,然后遍历方法列表,对比参数列表,如果参数列表一致,则返回该方法,否则继续遍历
        //  候选方法: 接收处理null值和子类,但是需要注意的是,如果存在多个方法参数列表一致,则无法确定调用哪个方法,所以会抛出异常
        List<Class<?>> argTypes = args.stream()
                .map(a -> Optional.ofNullable(a)
                        .map(Object::getClass)
                        .orElse(null))
                .collect(Collectors.toList());
        Class<?> clazz = o.getClass();
        Method[] methods = clazz.getMethods();
        Method candidateMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName) &&filter.test(method)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != args.size()) {
                    continue;
                }
                boolean isFullMatch = true;
                boolean isPartialMatch = false;
                // 尝试匹配参数类型
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> pt = parameterTypes[i];
                    Class<?> at = argTypes.get(i);
                    if (!pt.equals(at)) {
                        isFullMatch = false;
                        if (!ReflectionHelper.isAssignableFrom(pt, at)) {
                            // 无法分配,跳过此处方法验证
                            isPartialMatch = false;
                            break;
                        }
                    }
                    isPartialMatch = true;
                }
                if (isFullMatch) {
                    return method;
                }
                if (isPartialMatch ) {
                    if (Objects.nonNull(candidateMethod)) {
                        throw new PropertyAccessorRuntimeException("can not determine which method to invoke,"
                                                                   + "multiple methods found with same name:["
                                                                   + methodName
                                                                   + "]"
                                                                   + "and same parameter types:①:["
                                                                   + toString(candidateMethod.getParameterTypes())
                                                                   + "],②:["
                                                                   + toString(method.getParameterTypes())
                                                                   + "]");
                    }
                    candidateMethod = method;
                }


            }
        }
        if (Objects.isNull(candidateMethod)) {
            throw new PropertyAccessorRuntimeException("can not find method:[" + methodName + "] from:["
                                                       + clazz.getName()
                                                      + "] with args:[" + toString(argTypes) + "]");
        }
        return candidateMethod;
    }
    public static String toString(List<Class<?>> classes){
        if (Objects.isNull(classes)) {
            return "null";
        }
        return toString(classes.toArray(new Class[0]));
    }
    public static String toString(Class<?>[] classes) {
        if (Objects.isNull(classes)) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("[");
        String cs = Stream.of(classes)
                .map(Class::getName)
                .collect(Collectors.joining(","));
        sb.append(cs);
        return sb.append("]").toString();
    }
}
