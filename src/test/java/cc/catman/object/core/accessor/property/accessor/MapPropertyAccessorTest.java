package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MapPropertyAccessorTest {

    @Test
    public void test() {
        MapPropertyAccessor accessor = new MapPropertyAccessor(ObjectPathConfiguration.create(),"key");
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        assertEquals("value", accessor.get(map));

        accessor.set(map, "new_value");
        assertEquals("new_value", accessor.get(map));

        MapPropertyAccessor nonExistKeyAccessor = new MapPropertyAccessor(ObjectPathConfiguration.create(),"non_exist_key");
        Object nullValue = nonExistKeyAccessor.get(map);
        assertNull(nullValue);
    }

}