package cc.catman.object.core;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.MapObjectAccessor;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import cc.catman.object.core.accessor.property.wrapper.PropertyWrapperFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Map对象访问器测试
 */
public class MapObjectAccessorTest {
    ObjectPathConfiguration cfg = ObjectPathConfiguration.create();
    PropertyWrapperFactory wf = cfg.getWrapperFactory();
    @Test
    public void testIsSupport() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        Assert.assertTrue(objectAccessor.isSupport(createMap(), "a"));
    }

    @Test
    public void unSupport() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        Assert.assertFalse(objectAccessor.isSupport(wf.create(new Object()), "a"));
    }

    @Test
    public void testGet() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        Assert.assertEquals("b", objectAccessor.get(createMap(), "a").read());
    }
    @Test
    public void testGetNull() {
        MapObjectAccessor objectAccessor = new MapObjectAccessor();
        Assert.assertNull(objectAccessor.get(createMap(), "c").read());
    }


    private PropertyWrapper createMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "b");
        return wf.create(map);
    }
}