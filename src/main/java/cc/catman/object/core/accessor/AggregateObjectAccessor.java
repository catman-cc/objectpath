package cc.catman.object.core.accessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.Entity;
import cc.catman.object.core.accessor.property.PropertyWrapper;

/**
 * 聚合对象访问器
 * 
 * @author jpanda
 * @since 0.0.1
 */
public class AggregateObjectAccessor extends AbstractObjectAccessor {

    /**
     * 访问器
     */
    private final List<ObjectAccessor> accessors = new ArrayList<>();

    public AggregateObjectAccessor add(ObjectAccessor accessor) {
        Optional.ofNullable(this.configuration).ifPresent(c -> c.inject(accessor));
        this.accessors.add(accessor);
        return this;
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        super.injectConfiguration(configuration);
        this.accessors.forEach(a -> a.injectConfiguration(configuration));
    }

    @Override
    public boolean isSupport(PropertyWrapper object, Object key, EAccessorKind kind) {
        return this.accessors.stream().anyMatch(accessor -> accessor.isSupport(object, key, kind));
    }

    @Override
    public PropertyWrapper get(PropertyWrapper object, Object key) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, key)).findFirst()
                .map(accessor -> accessor.get(object, key)).orElse(null);
    }

    @Override
    public void eachKey(PropertyWrapper object, Consumer<Object> consumer) {
        this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_KEY))
                .findFirst().ifPresent(accessor -> accessor.eachKey(object, consumer));
    }

    @Override
    public void eachValue(PropertyWrapper object, Consumer<Object> consumer) {
        this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_VALUE))
                .findFirst().ifPresent(accessor -> accessor.eachValue(object, consumer));
    }

    @Override
    public void eachEntry(PropertyWrapper object, Consumer<Entity> consumer) {
        this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_ENTRY))
                .findFirst().ifPresent(accessor -> accessor.eachEntry(object, consumer));
    }

    @Override
    public PropertyWrapper filter(PropertyWrapper object, Predicate<Object> filter) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.FILTER))
                .findFirst().map(accessor -> accessor.filter(object, filter)).orElse(null);
    }

    @Override
    public PropertyWrapper map(PropertyWrapper object, Function<Object, Object> mapper) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.MAP))
                .findFirst()
                .map(accessor -> accessor.map(object, mapper))
                .orElse(null);
    }

    @Override
    public int size(PropertyWrapper object) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, null))
                .findFirst().map(accessor -> accessor.size(object)).orElse(-1);
    }

    @Override
    public List<Object> covertToList(PropertyWrapper object) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.COVERT_TO_LIST))
                .findFirst().map(accessor -> accessor.covertToList(object)).orElse(null);
    }
}
