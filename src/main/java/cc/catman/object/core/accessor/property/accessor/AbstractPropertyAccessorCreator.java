package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;

import java.util.Objects;

/**
 * 抽象 公共 属性访问器 创建器
 * @author jpanda
 * @since 0.0.3
 */
public abstract class AbstractPropertyAccessorCreator implements PropertyAccessorCreator{

    protected ObjectPathConfiguration configuration;

    @Override
    public int score(Object object, Object indexOrName) {
        if (Objects.isNull(object)){
            return -1;
        }
        return this.score(object.getClass(),indexOrName);
    }

    @Override
    public int score(PropertyWrapper belong, Object indexOrName) {
        Class<?> t = belong.readType();
        if (Objects.isNull(t)){
            return this.score(t,indexOrName);
        }
        return -1;
    }

    @Override
    public PropertyAccessor create(Object belong, Object indexOrKeyName) {
        if (Objects.isNull(belong)){
            throw new PropertyAccessorRuntimeException("unable create property accessor for null value");
        }
        return this.create(belong.getClass(),indexOrKeyName);
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        this.configuration=configuration;
    }
}
