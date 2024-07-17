package cc.catman.object.core.accessor.property.accessor;

import cc.catman.object.ObjectPathConfiguration;
import cc.catman.object.core.exception.PropertyAccessorRuntimeException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SetPropertyAccessorTest {

    @Test
    public void test() {
        ObjectPathConfiguration config = ObjectPathConfiguration.create();
        SetPropertyAccessor accessor = new SetPropertyAccessor(config,0);
        Set<String> set = new HashSet<>(Arrays.asList("0", "1", "2", "3"));
        assertEquals("0", accessor.get(set));
        boolean found = false;
        try {
            accessor.set(set, "4");
        } catch (PropertyAccessorRuntimeException e) {
            found=true;
        }
        assertTrue(found);

        config.setAllowWriteForSet(true);
        accessor.set(set, "4");
        assertEquals("4", accessor.get(set));
    }

}