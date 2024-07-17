package cc.catman.object.core.accessor;

import cc.catman.object.core.Entity;
import cc.catman.object.core.accessor.property.PropertyWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Map对象访问器,支持Map的访问
 * @author jpanda
 * @since 2021-04-23
 */
public class MapObjectAccessor  extends AbstractObjectAccessor {
    /**
     * 要求object必须是Map类型,我们常见的一些对象其实也是Map类型,比如Properties/Hashtable等
     * key值理论上可以是任意类型,所以不会对key值进行验证
     */
    @Override
    public boolean isSupport(PropertyWrapper object, Object key, EAccessorKind kind) {
        return object.isNotNull()&&object.isInstanceOf(Map.class);
    }

    @Override
    public PropertyWrapper get(PropertyWrapper object, Object key) {
        if (object.isInstanceOf(Map.class)){
            return object.read(key);
        }
        return object.wrapper(null);
    }

    @Override
    public PropertyWrapper filter(PropertyWrapper object, Predicate<Object> filter) {

        if (object.isInstanceOf(Map.class)) {
            Map<Object, Object> map = new HashMap<>();
            ((Map<?, ?>) object.read(Map.class)).forEach((k, v) -> {
                if (filter.test(v)) {
                    map.put(k, v);
                }
            });
            return object.wrapper(map);
        }
        return object.wrapper(null);
    }

    @Override
    public void eachEntry(PropertyWrapper object, Consumer<Entity> consumer) {
        if (object.isInstanceOf(Map.class)) {
            ((Map<?, ?>) object.read(Map.class)).forEach((k, v) -> consumer.accept(Entity.builder()
                            .key(k)
                            .value(v)
                    .build()

            ));
        }
    }
}
