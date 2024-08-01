package cc.catman.object.core.accessor.convert;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.ConfigurationAccessor;
/**
 * 类型转换接口
 * @author jpanda
 * @since 0.0.3
 */
public interface IConvert extends ConfigurationAccessor {

    <T> T convert(Object o,Class<T> clazz);

    @Override
    default void injectConfiguration(ObjectPathConfiguration configuration){}
}
