package cc.catman.object.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 类型转换工具类
 * @author jpanda
 * @since 0.0.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CastHelper {

    /**
     * 尝试将一个对象转换为int类型
     * @param obj 对象
     * @return int类型的值
     */
    public static Integer castToInt(Object obj) {
        if (obj instanceof Integer){
            return (Integer) obj;
        }
        if (obj instanceof Number){
            return ((Number) obj).intValue();
        }
        if (obj instanceof String){
            return Integer.parseInt((String) obj);
        }
        throw new IllegalArgumentException("Cannot cast to int: " + obj);
    }
}
