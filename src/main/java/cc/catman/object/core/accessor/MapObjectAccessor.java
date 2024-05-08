package cc.catman.object.core.accessor;

import cc.catman.object.core.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Map对象访问器,支持Map的访问
 * @author jpanda
 * @since 2021-04-23
 */
public class MapObjectAccessor implements SpecifyObjectAccessor{
    /**
     * 要求object必须是Map类型,我们常见的一些对象其实也是Map类型,比如Properties/Hashtable等
     * key值理论上可以是任意类型,所以不会对key值进行验证
     */
    @Override
    public boolean isSupport(Object object, Object key, EAccessorKind kind) {
        return object instanceof Map;
    }

    @Override
    public Object get(Object object, Object key) {
        if (object instanceof Map) {
            return ((Map<?, ?>) object).get(key);
        }
        return null;
    }

    @Override
    public Object filter(Object object, Predicate<Object> filter) {

        if (object instanceof Map) {
            Map<Object, Object> map = new HashMap<>();
            ((Map<?, ?>) object).forEach((k, v) -> {
                if (filter.test(v)) {
                    map.put(k, v);
                }
            });
            return map;
        }
        return null;
    }

    @Override
    public void eachEntry(Object object, Consumer<Entity> consumer) {
        if (object instanceof Map) {
            ((Map<?, ?>) object).forEach((k, v) -> {
                consumer.accept(Entity.builder()
                                .key(k)
                                .value(v)
                        .build()

                );
            });
        }

    }
}
