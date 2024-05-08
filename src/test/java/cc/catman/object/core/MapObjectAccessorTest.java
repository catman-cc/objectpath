package cc.catman.object.core;

import cc.catman.object.core.accessor.MapObjectAccessor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Map对象访问器测试
 */
public class MapObjectAccessorTest {

    @Test
    public void testIsSupport() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        assert objectAccessor.isSupport(createMap(), "a");
    }

    @Test
    public void unSupport() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        assert !objectAccessor.isSupport(null, "a");
    }

    @Test
    public void testGet() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        assert objectAccessor.get(createMap(), "a").equals("b");
    }
    @Test
    public void testGetNull() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        assert objectAccessor.get(null, "a") == null;
    }


    private Map<Object, Object> createMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        return map;
    }
}