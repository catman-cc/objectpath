package cc.catman.object.core.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectionHelperTest {

    @Test
    public void isAssignableFrom() {
        assertTrue(ReflectionHelper.isAssignableFrom(Map.class, Map.class));
        assertTrue(ReflectionHelper.isAssignableFrom(Object.class,Map.class));
        assertTrue(ReflectionHelper.isAssignableFrom(Map.class,LinkedHashMap.class));
        assertTrue(ReflectionHelper.isAssignableFrom(List.class, ArrayList.class));
    }
}