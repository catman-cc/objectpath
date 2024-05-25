package cc.catman.object.core;

import cc.catman.object.ObjectPathConfiguration;

/**
 * 配置对象访问器,用于注入配置对象
 * @author jpanda
 * @since 0.0.2
 */
public interface ConfigurationAccessor {

    void injectConfiguration(ObjectPathConfiguration configuration);
}
