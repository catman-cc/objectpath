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
import cc.catman.object.core.cache.ICache;

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

    private ICache<ObjectAccessor> cache;


    public AggregateObjectAccessor add(ObjectAccessor accessor) {
        Optional.ofNullable(this.configuration).ifPresent(c -> c.inject(accessor));
        this.accessors.add(accessor);
        return this;
    }

    @Override
    public void injectConfiguration(ObjectPathConfiguration configuration) {
        super.injectConfiguration(configuration);
        this.accessors.forEach(a -> a.injectConfiguration(configuration));
        this.cache=configuration.getObjectAccessorCache();
    }

    @Override
    public boolean isSupport(PropertyWrapper object, Object key, EAccessorKind kind) {
        return this.accessors.stream().anyMatch(accessor -> accessor.isSupport(object, key, kind));
    }

    @Override
    public PropertyWrapper get(PropertyWrapper object, Object key) {
        return this.getAccessor(object, key).map(accessor -> accessor.get(object, key)).orElse(null);
    }

    @Override
    public void eachKey(PropertyWrapper object, Consumer<Object> consumer) {
        getAccessor(object, EAccessorKind.EACH_KEY).ifPresent(accessor -> accessor.eachKey(object, consumer));
    }

    @Override
    public void eachValue(PropertyWrapper object, Consumer<Object> consumer) {
        getAccessor(object, EAccessorKind.EACH_VALUE).ifPresent(accessor -> accessor.eachValue(object, consumer));
    }

    @Override
    public void eachEntry(PropertyWrapper object, Consumer<Entity> consumer) {
        getAccessor(object, EAccessorKind.EACH_ENTRY).ifPresent(accessor -> accessor.eachEntry(object, consumer));
    }

    @Override
    public PropertyWrapper filter(PropertyWrapper object, Predicate<Object> filter) {
       return getAccessor(object, EAccessorKind.FILTER).map(accessor -> accessor.filter(object, filter)).orElse(null);
    }

    @Override
    public PropertyWrapper map(PropertyWrapper object, Function<Object, Object> mapper) {
        return getAccessor(object,EAccessorKind.MAP)
                .map(accessor->accessor.map(object,mapper))
                .orElse(null);
    }

    @Override
    public int size(PropertyWrapper object) {
        return getAccessor(object,null).map(accessor -> accessor.size(object)).orElse(-1);
    }

    @Override
    public List<Object> covertToList(PropertyWrapper object) {
        return getAccessor(object,EAccessorKind.COVERT_TO_LIST)
                .map(accessor->accessor.covertToList(object))
                .orElse(null);
    }

    public Optional<ObjectAccessor> getAccessor(PropertyWrapper object, Object key) {
        return Optional.ofNullable(cache.getOrSet(object.readType(), key, () -> this.accessors.stream().parallel().filter(accessor -> accessor.isSupport(object, key)).findFirst().orElse(null)));
    }
}
