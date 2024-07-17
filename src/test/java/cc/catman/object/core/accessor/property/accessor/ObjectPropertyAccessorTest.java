package cc.catman.object.core.accessor.property.accessor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.accessor.property.PropertyWrapper;
import lombok.SneakyThrows;

public class ObjectPropertyAccessorTest {

    @Test
    @SneakyThrows
    public void test() {
        ObjectPropertyAccessor accessor = new ObjectPropertyAccessor(
                ObjectPathConfiguration.create(),
                "name");

        TestObject obj = new TestObject();
        obj.name = "John";
        obj.features = new ArrayList<>(Arrays.asList("1", "2", "3"));
        assertEquals("John", accessor.get(obj));
        accessor.set(obj, "Jane");
        assertEquals("Jane", accessor.get(obj));

        ObjectPropertyAccessor childAccessor = new ObjectPropertyAccessor(
                ObjectPathConfiguration.create(),
                "features");
        PropertyWrapper pw = childAccessor.get(obj, "0");

        assertEquals("1", pw.read());

        boolean set = pw.set("new-value");
        assertTrue(set);
        assertEquals("new-value", pw.read());

        TestObject to = new TestObject();
        ObjectPropertyAccessor toCA = new ObjectPropertyAccessor(
                ObjectPathConfiguration.create(),
                "features");
        PropertyWrapper toPW = toCA.get(to, "0");
        toPW.set("1");
        assertNotNull(to.features);
        assertEquals("1",to.features.get(0));

    }

    public static class TestObject {
        private String name;
        private List<String> features;
    }
}