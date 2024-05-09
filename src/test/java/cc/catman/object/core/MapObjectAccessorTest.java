package cc.catman.object.core;

import cc.catman.object.core.accessor.MapObjectAccessor;
import org.junit.Assert;
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
        Assert.assertTrue(objectAccessor.isSupport(createMap(), "a"));
    }

    @Test
    public void unSupport() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(new Object(), "a"));
    }

    @Test
    public void testGet() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        Assert.assertEquals("b", objectAccessor.get(createMap(), "a"));
    }
    @Test
    public void testGetNull() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        Assert.assertNull(objectAccessor.get(createMap(), "c"));
    }


    private Map<Object, Object> createMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        return map;
    }
}