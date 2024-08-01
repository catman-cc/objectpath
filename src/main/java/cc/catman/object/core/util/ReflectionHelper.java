package cc.catman.object.core.util;

import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        ||Objects.isNull(sourceType)
        ){
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

    /**
     * 为字段设置可访问性
     * @param field 字段
     */
    @SuppressWarnings("java:S3011")
    public static void makeAccessible(Field field) {
        if (field.isAccessible()){
            return;
        }
        field.setAccessible(true);
    }

    /**
     * 尝试查找字段
     * @param type 类型
     * @param fieldName 字段名称
     * @return 字段
     */
    public static Field findField(Class<?> type,String fieldName){
        fieldName=StringHelper.unquote(fieldName);
        if (Objects.nonNull(fieldName)){
            try {
                Field f = type.getDeclaredField(fieldName);
                makeAccessible(f);
                return f;
            } catch (NoSuchFieldException ignored) {
                // ignored
            }
        }
        throw new PropertyAccessorRuntimeException("can not find field:"+fieldName+"with:"+type.getName());
    }
}
