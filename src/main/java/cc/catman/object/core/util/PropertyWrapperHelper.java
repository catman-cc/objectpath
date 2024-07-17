package cc.catman.object.core.util;

import cc.catman.object.core.accessor.property.PropertyWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 属性包装器助手
 * @author jpanda
 * @since 0.0.3
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyWrapperHelper {

    public static Object unWrapper(Object object){
        if (object instanceof PropertyWrapper){
            return ((PropertyWrapper) object).read();
        }
        return object;
    }
}
