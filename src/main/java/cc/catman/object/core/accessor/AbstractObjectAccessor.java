package cc.catman.object.core.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.ConfigurationAccessor;

/**
 * 抽象对象访问器
 * @author jpanda
 * @since 0.0.2
 */
public abstract class AbstractObjectAccessor implements ObjectAccessor, ConfigurationAccessor {

    /**
     * 配置对象
     */
    protected ObjectPathConfiguration configuration;

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration=configuration;
    }
}
