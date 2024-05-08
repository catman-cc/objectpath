package cc.catman.object.core.accessor;

import cc.catman.object.core.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
/**
 * 聚合对象访问器
 * @author jpanda
 * @since 0.0.1
 */
public class AggregateObjectAccessor implements SpecifyObjectAccessor{

    /**
     * 访问器
     */
    private final List<SpecifyObjectAccessor> accessors=new ArrayList<>();

    public AggregateObjectAccessor add(SpecifyObjectAccessor accessor){
        this.accessors.add(accessor);
        return this;
    }
    @Override
    public boolean isSupport(Object object, Object key, EAccessorKind kind) {
        return this.accessors.stream().anyMatch(accessor -> accessor.isSupport(object, key, kind));
    }

    @Override
    public Object get(Object object, Object key) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, key)).findFirst()
                .map(accessor -> accessor.get(object, key)).orElse(null);
    }

    @Override
    public void eachKey(Object object, Consumer<Object> consumer) {
        this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_KEY))
                .findFirst().ifPresent(accessor -> accessor.eachKey(object, consumer));
    }

    @Override
    public void eachValue(Object object, Consumer<Object> consumer) {
        this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_VALUE))
                .findFirst().ifPresent(accessor -> accessor.eachValue(object, consumer));
    }

    @Override
    public void eachEntry(Object object, Consumer<Entity> consumer) {
        this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.EACH_ENTRY))
                .findFirst().ifPresent(accessor -> accessor.eachEntry(object, consumer));
    }

    @Override
    public Object filter(Object object,  Predicate<Object> filter) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.FILTER))
                .findFirst().map(accessor -> accessor.filter(object, filter)).orElse(null);
    }

    @Override
    public Object map(Object object, Function<Object, Object> mapper) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.MAP))
                .findFirst()
                .map(accessor -> accessor.map(object, mapper))
                .orElse(null);
    }

    @Override
    public int size(Object object) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, null))
                .findFirst().map(accessor -> accessor.size(object)).orElse(-1);
    }

    @Override
    public List<Object> covertToList(Object object) {
        return this.accessors.stream().filter(accessor -> accessor.isSupport(object, EAccessorKind.COVERT_TO_LIST))
                .findFirst().map(accessor -> accessor.covertToList(object)).orElse(null);
    }
}
