package cc.catman.object.core.json;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.ConfigurationAccessor;

/**
 * 抽象json编码器
 * @author jpanda
 * @since 0.0.1
 */
public abstract class AbstractJsonCoder implements JsonCoder, ConfigurationAccessor {
    protected ObjectPathConfiguration configuration;

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration=configuration;
    }
}
