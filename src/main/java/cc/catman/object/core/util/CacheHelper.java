package cc.catman.object.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 缓存帮助类
 * @author jpanda
 * @since 0.0.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheHelper {
    private static final Object NULL_OBJECT=new Object();

    public static Object ensureNotNull(Object object){
        if (Objects.isNull(object)){
            return NULL_OBJECT;
        }
        return object;
    }
}
